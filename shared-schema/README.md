# Shared Schema Implementation

Sample Spring Boot REST API with shared-schema based Multi-tenancy and Flyway migrations.

## Approach

Approach:

- Uses hibernate filter to limit the query results based on tenant.
- Uses Jpa Entity Listeners to enforce tenant details during creating/updating entities.
- Uses Spring AOP (AspectJ) to set the filter parameters.

## Database Setup

Create **shared-schema** database in postgres. 

When the application start-up [flyway](https://flywaydb.org/getstarted/) migration scripts will create the necessary tables.

![Shared Schema](../images/shared-schema-ERD.png "Shared Schema")

## Configuring EntityManager


```java

@Configuration
@EnableJpaRepositories(basePackages = "org.imaginea.multitenancy.database")
public class JpaConfig {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private JpaProperties jpaProperties;

  private JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setPackagesToScan(JpaConfig.class.getPackage().getName());
    em.setJpaVendorAdapter(this.jpaVendorAdapter());
    em.setDataSource(dataSource);

    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DISCRIMINATOR);
    // Configure any Hibernate Properties
    em.setJpaPropertyMap(jpaPropertiesMap);

    return em;
  }

  
  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }

}
```

## Creating the Hibernate Filter and Jpa Listener ()

```java

/**
 * The type Tenant entity.
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "long")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenant")
public abstract class TenantEntity extends BaseEntity {

  public static final String TENANT_COLUMN = "tenant_id";
  /**
   * Name of the tenant to which the user belongs
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = TENANT_COLUMN, updatable = false)
  @JsonIgnore
  private Tenant tenant;

  /**
   * Gets tenant.
   *
   * @return the tenant
   */
  public Tenant getTenant() {
    return tenant;
  }

  /**
   * Sets tenant.
   *
   * @param tenant the tenant
   */
  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  @PrePersist
  @PreUpdate
  @PreRemove
  public void onEntityChange() {
    this.tenant = TenantContextHolder.getContext().getTenant();
  }

}

```

Notice that our abstract TenantEntity is a @MappedSuperclass to prevent a table being generated for it and its @Inheritance strategy will create a table per class that inherits from this object.

onEntityChange() method being called for any new entity or update on existing entities. For more information [JPA Callbacks](https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#events-jpa-callbacks).


## Multi Tenancy Annotations

|Type                 |Description                       |
|:----                |:-----                            |
|@CurrentTenant        |Resolves the current tenant and binds a Hibernate session for the scope of the method|
|@Tenant               |Resolves a specific tenant and binds a Hibernate session for the scope of the method|
|@WithoutTenant        |Execute some logic within a method without a tenant present (Admin Queries).|

### MultiTenantAspect

```java

@Aspect
@Component
public class MultiTenantAspect {

  @Pointcut(value = "@within(org.imaginea.multitenancy.annotations.CurrentTenant) || @annotation(org.imaginea.multitenancy.annotations.CurrentTenant)")
  void hasCurrentTenantAnnotation() {}

  @Pointcut(value = "@within(org.imaginea.multitenancy.annotations.Tenant) || @annotation(org.imaginea.multitenancy.annotations.Tenant)")
  void hasTenantAnnotation() {}

  @Pointcut(value = "@within(org.imaginea.multitenancy.annotations.WithoutTenant) || @annotation(org.imaginea.multitenancy.annotations.WithoutTenant)")
  void hasWithoutTenantAnnotation() {}

  @Pointcut(value = "hasCurrentTenantAnnotation() || hasTenantAnnotation() || hasWithoutTenantAnnotation()")
  void hasMultiTenantAnnotation() {}

  @PersistenceContext
  public EntityManager entityManager;

  @Around("execution(public * *(..)) && hasMultiTenantAnnotation()")
  public Object aroundExecution(ProceedingJoinPoint pjp) throws Throwable {
    final String methodName = pjp.getSignature().getName();
    final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Method method = methodSignature.getMethod();
    if (method.getDeclaringClass().isInterface()) {
      method = pjp.getTarget().getClass().getDeclaredMethod(methodName, method.getParameterTypes());
    }
    Annotation multiTenantAnnotation = getMultiTenantAnnotation(method);
    if (multiTenantAnnotation == null) {
      multiTenantAnnotation = getMultiTenantAnnotation(method.getDeclaringClass());
    }
    if (multiTenantAnnotation != null && !(multiTenantAnnotation instanceof WithoutTenant)) {
      Serializable tenantId = TenantContextHolder.getContext().getTenantId();
      if (multiTenantAnnotation instanceof Tenant) {
        tenantId = Long.parseLong(((Tenant) multiTenantAnnotation).value());
      }
      org.hibernate.Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
      filter.setParameter("tenantId", tenantId);
      filter.validate();
    }
    return pjp.proceed();
  }

  private Annotation getMultiTenantAnnotation(AnnotatedElement element) {
    Annotation annotation = element.getAnnotation(CurrentTenant.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = element.getAnnotation(Tenant.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = element.getAnnotation(WithoutTenant.class);
    if (annotation != null) {
      return annotation;
    }
    return null;
  }
}


```

### Configuring Service Class

- configure the service Class or method with Any of these Annotation (Based on Usage.)
- Method of annotated class or annotated Method execution will be intercepted and tenantFilter will be enabled for the Hibernate Session.
- Also All service Class or method needs to Annotated with @Transactional. So that MultiTenantAspect can get the current Hibernate session and enable the TenantFilter.

##### Example:

```java

@Service
@Transactional(readOnly = true)
@CurrentTenant
public class ContactServiceImpl implements ContactService {
  
  @Autowired
  private ContactRepository repository;

  
  @Override
  public Page<Contact> getContacts(Long listId, int page, int limit) {
    Pageable pageableRequest = PageRequest.of(page, limit);
    return repository.findAllByContactListId(listId, pageableRequest);
  }
  
  // Sample Admin Service
  @Override
  @WithoutTenant
  public Page<Contact> getContacts(int page, int limit) {
    Pageable pageableRequest = PageRequest.of(page, limit);
    return repository.findAll();
  }
    
}

```

For more information [See](src)


