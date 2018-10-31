package org.imaginea.multitenancy.database.lms;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "org.imaginea.multitenancy.database.lms", entityManagerFactoryRef = "lmsEntityManagerFactory",
    transactionManagerRef = "lmsTransactionManager")
@DependsOn("adminTransactionManager")
public class LmsJpaConfig {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private DataSourceProperties dsProperties;

  @Autowired
  private JpaProperties jpaProperties;

  @Autowired
  private MultiTenantConnectionProvider multiTenantConnectionProvider;

  @Autowired
  private CurrentTenantIdentifierResolver tenantIdentifierResolver;

  private JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean lmsEntityManagerFactory() {

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setPackagesToScan(LmsJpaConfig.class.getPackage().getName());
    em.setJpaVendorAdapter(this.jpaVendorAdapter());

    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
    em.setJpaPropertyMap(jpaPropertiesMap);

    return em;
  }

  @Bean
  @Primary
  public PlatformTransactionManager lmsTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(lmsEntityManagerFactory().getObject());
    return transactionManager;
  }
}
