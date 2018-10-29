package org.imaginea.multitenancy.database;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.imaginea.multitenancy.database.lms.TenantSchemaResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

  public static final String DEFAULT_SCHEMA = "lms";

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DataSource dataSource;

  @Bean
  public Flyway flyway() {
    logger.info("Migrating default schema ");
    Flyway flyway = new Flyway();
    flyway.setLocations("db/migration/default");
    flyway.setDataSource(dataSource);
    flyway.setSchemas(TenantSchemaResolver.DEFAULT_SCHEMA);
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();
    return flyway;
  }

}
