package org.imaginea.multitenancy.database.lms;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.imaginea.multitenancy.database.FlywayConfig;
import org.imaginea.multitenancy.database.admin.model.DataSourceConfig;
import org.imaginea.multitenancy.database.admin.model.Tenant;
import org.imaginea.multitenancy.database.admin.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private static Logger logger = LoggerFactory.getLogger(TenantConnectionProvider.class);

  @Autowired
  private DataSource datasource;

  @Autowired
  private DataSourceProperties dsProperties;

  @Autowired
  private TenantRepository tenantRepository;

  private Map<String, DataSource> tenantDataSources = new HashMap<>();

  @Override
  protected DataSource selectAnyDataSource() {
    return datasource;
  }

  @Override
  protected DataSource selectDataSource(String tenantIdentifier) {
    return tenantDataSources.computeIfAbsent(tenantIdentifier, this::getTenantDatabase);
  }

  private DataSource getTenantDatabase(String tenantIdentifier) {
    Tenant tenant = tenantRepository.findByName(tenantIdentifier).orElseThrow(() -> new RuntimeException("Invalid Tenant"));
    DataSourceConfig dataSourceConfig = tenant.getDataSourceConfig();
    DataSource dataSource = tenantDataSources.computeIfAbsent(tenant.getName(), s -> createDataSource(dataSourceConfig));
    executeFlywayMigration(dataSource);
    return dataSource;
  }

  @PostConstruct
  public void configureDataSources() {
    List<Tenant> tenants = tenantRepository.findAll();
    tenants.forEach(tenant -> {
      DataSource dataSource = createDataSource(tenant.getDataSourceConfig());
      executeFlywayMigration(dataSource);
      tenantDataSources.put(tenant.getName(), dataSource);
    });
  }

  private DataSource createDataSource(DataSourceConfig dataSourceConfig) {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(this.getClass().getClassLoader()).driverClassName(dsProperties.getDriverClassName())
        .url(dataSourceConfig.getDatabaseUrl()).username(dataSourceConfig.getUsername()).password(dataSourceConfig.getPassword());
    if (dsProperties.getType() != null) {
      dataSourceBuilder.type(dsProperties.getType());
    }
    return dataSourceBuilder.build();
  }


  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    Connection connection = super.getConnection(tenantIdentifier);
    connection.setSchema(TenantSchemaResolver.DEFAULT_SCHEMA);
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    logger.debug("Release connection for tenant {}", tenantIdentifier);
    releaseAnyConnection(connection);
  }

  private void executeFlywayMigration(DataSource dataSource) {
    Flyway flyway = new Flyway();
    flyway.setLocations("db/migration/tenants");
    flyway.setDataSource(dataSource);
    flyway.setSchemas(TenantSchemaResolver.DEFAULT_SCHEMA);
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();
  }
}
