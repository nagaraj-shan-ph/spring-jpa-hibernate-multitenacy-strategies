package org.imaginea.multitenancy.database.admin;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "org.imaginea.multitenancy.database.admin", entityManagerFactoryRef = "adminEntityManagerFactory",
    transactionManagerRef = "adminTransactionManager")
public class AdminJpaConfig {

  @Autowired
  private JpaProperties jpaProperties;

  @Autowired
  private DataSource dataSource;

  private JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory() {

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setPackagesToScan(AdminJpaConfig.class.getPackage().getName());
    em.setJpaVendorAdapter(this.jpaVendorAdapter());
    em.setDataSource(dataSource);

    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    jpaPropertiesMap.put(Environment.SHOW_SQL, "true");
    jpaPropertiesMap.put(Environment.HBM2DDL_AUTO, "update");
    em.setJpaPropertyMap(jpaPropertiesMap);

    return em;
  }

  @Bean
  public PlatformTransactionManager adminTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(adminEntityManagerFactory().getObject());
    return transactionManager;
  }
}
