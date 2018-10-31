# Multi-tenancy Implementations

Sample Spring Boot applications with Different multi tenancy approaches.

# Multi-tenancy

## What is Multi-tenancy

Multi-tenancy is a software architecture in which a single instance of a software  provides a service for multiple customers or _"tenants."_  A noteworthy example of multi-tenancy software is Software as a Service (SaaS). One server shared by many users, each with its own data.

# why multi-tenancy?
One of the main reasons is multi-tenancy allows us to scale and serve a large number of clients using shared computing resources. Another reason is that tenants are served from the same application stack. This simplifies the overall deployment and operational complexity, and reduces cost associated with operations.

## Different Multi-tenancy implementation – software architecture 
Multi-tenant applications which require tenant data isolation in the database layer. There are different approaches for achieving this data isolation.
The most widely used approaches are

- **Shared schema:** Tenants share common schema and are distinguished by a tenant **discriminator** column.
- **Separate schemas:** Tenants share common database but each tenant has its own set of tables (schema).
- **Separate-databases:** Each tenant has its own database.

### Shared schema:
Shared schema approach is the easiest and most cost-effective. Here, all the tenants share the same database, the same schema and even the same tables. As a result, data records for all the tenants are stored together in these common tables. Each of these common tables have an indexed tenant ID column which is used to retrieve records belonging to a specific tenant.

![Shared Schema](images/shared-schema.png "Shared Schema")

### Separate schemas:
Separate schema per tenant approach, introduces physical separation between tenants while still using the same database, but creating a new schema for every tenant. All the tables belonging to the tenant then reside within that schema and the data for the tenant is somewhat isolated from other tenant data

![Separate Schema](images/separate-schema.png "Separate Schema")

### Separate-databases
Each tenant's data is kept in a physically separate database instance. This is the highest level of isolation. It adds further isolation between the tenants by creating a brand new database for every tenant. Each of these databases have the same schema and migrations are run on all the tenant databases during deployment.

![Separate Database](images/separate-database.png "Separate Database")

# Multi-tenancy in Hibernate
Hibernate officially supports two different multi-tenancy mechanisms:

- **separate schema:** every tenant has its own schema in a shared database
- **separate database:** every tenant has its own database instance

Unfortunately, both of these mechanisms come with some downsides in terms of scaling. A third Hibernate multi-tenancy mechanism, a tenant discriminator, also exists, and it’s usable—but it’s still considered a work-in-progress by some.

There is a open issue [HHH-6054](https://hibernate.atlassian.net/browse/HHH-6054) to Support for discriminator-based multi-tenancy in hibernate.

Hibernate provides filters which allow for parameterized data to be used in a conditional to determine if queried data should be returned or not. With this we can implement discriminator-based multi-tenant application.

## Implementation

In this repo I have included Implementation details for all three approaches

## Technologies and frameworks used:

- Java 1.8
- Gradle
- Spring boot 2
- Hibernate
- Postgres 9.6 or greater
- Flyway

## Technologies and frameworks used:

## Common Implementation Details:

### TenantContextHolder

We need one place, where tenant name will be stored. It should be available across threads, service classes, simply anywhere, where tenant specific code could be executed. 

```java

public abstract class TenantContextHolder {

  private static ThreadLocal<TenantContext> tenants;

  private static ThreadLocal<TenantContext> inheritableTenants;

  static {
    tenants = new NamedThreadLocal<>("tenants Context");
    inheritableTenants = new NamedInheritableThreadLocal<>("tenants Context");
  }

  public static void reset() {

    tenants.remove();
    inheritableTenants.remove();
  }

  public static TenantContext getContext() {
    TenantContext context = tenants.get();
    if (context == null) {
      context = inheritableTenants.get();
    }
    return context;
  }

  public static void setTenant(Tenant tenant) {
    TenantContext tenantContext = new TenantContext(tenant);
    setTenant(tenantContext);
  }

  public static void setTenant(Tenant tenant, boolean inheritable) {
    TenantContext tenantContext = new TenantContext(tenant);
    setTenant(tenantContext, inheritable);
  }

  public static void setTenant(TenantContext tenantContext) {
    setTenant(tenantContext, false);
  }

  public static void setTenant(TenantContext tenantContext, boolean inheritable) {
    if (tenantContext == null) {
      reset();
    } else {
      if (inheritable) {
        inheritableTenants.set(tenantContext);
        tenants.remove();
      } else {
        tenants.set(tenantContext);
        inheritableTenants.remove();
      }
    }
  }
}

```
The TenantContextHolder will hold the tenant information across the threads for each request. At the end of each request this will be clear out.


### TenantInterceptor

An interceptor that reads the tenant identifier from the request header (or JWT in different implementations) and sets the tenant context.

```java

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

  public static final String TENANT_HEADER = "X-TenantID";
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  TenantRepository repository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String tenantHeader = request.getHeader(TENANT_HEADER);
    boolean tenantSet = false;
    if (!tenantHeader.isEmpty()) {
      Tenant tenant = repository.findByName(tenantHeader).orElseThrow(() -> new RuntimeException("Invalid Tenant :" + tenantHeader));
      logger.debug("Set TenantContextHolder: {}", tenant);
      TenantContextHolder.setTenant(tenant, true);
      tenantSet = true;
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"error\": \"No tenant supplied\"}");
      response.getWriter().flush();
    }
    return tenantSet;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    logger.debug("Clear TenantContextHolder: {}", TenantContextHolder.getContext());
    TenantContextHolder.reset();
  }
}

```