package org.imaginea.multitenancy.database.lms;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.imaginea.multitenancy.database.admin.model.DataSourceConfig;
import org.imaginea.multitenancy.database.admin.model.Tenant;
import org.imaginea.multitenancy.database.admin.repository.DataSourceConfigRepository;
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

  @Autowired
  private DataSourceConfigRepository dataSourceConfigRepository;

  private Map<String, DataSource> tenantDataSources = new HashMap<>();

  private Map<String, String> tenantDatabases = new ConcurrentHashMap<>();

  private Map<String, String> tenantSchemas = new ConcurrentHashMap<>();

  @Override
  protected DataSource selectAnyDataSource() {
    return datasource;
  }

  @Override
  protected DataSource selectDataSource(String tenantIdentifier) {
    String database = tenantDatabases.computeIfAbsent(tenantIdentifier, this::getTenantDatabase);
    return tenantDataSources.get(database);
  }

  private String getTenantDatabase(String tenantIdentifier) {
    Tenant tenant = tenantRepository.findByName(tenantIdentifier).orElseThrow(() -> new RuntimeException("Invalid Tenant"));
    DataSourceConfig dataSourceConfig = tenant.getDataSourceConfig();
    DataSource dataSource = tenantDataSources.computeIfAbsent(dataSourceConfig.getName(), s -> createDataSource(dataSourceConfig));
    tenantSchemas.put(tenant.getName(), tenant.getSchemaName());
    executeFlywayMigration(tenant, dataSource);
    return tenant.getDataSourceConfig().getName();
  }

  @PostConstruct
  public void configureDataSources() {
    List<DataSourceConfig> dataSourceConfigs = dataSourceConfigRepository.findAll();
    dataSourceConfigs.forEach(dataSourceConfig -> tenantDataSources.put(dataSourceConfig.getName(), createDataSource(dataSourceConfig)));
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
    connection.setSchema(tenantSchemas.get(tenantIdentifier));
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    logger.debug("Release connection for tenant {}", tenantIdentifier);
    releaseAnyConnection(connection);
  }

  private void executeFlywayMigration(Tenant tenant, DataSource dataSource) {
    String schema = tenant.getSchemaName();
    Flyway flyway = new Flyway();
    flyway.setLocations("db/migration/tenants");
    flyway.setDataSource(dataSource);
    flyway.setSchemas(schema);
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();
  }
}
