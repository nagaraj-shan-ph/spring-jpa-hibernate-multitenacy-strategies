package org.imaginea.multitenancy.database;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantAwareRoutingDataSource extends AbstractRoutingDataSource {

  private final DataSourceProperties dsProperties;

  public TenantAwareRoutingDataSource(DataSourceProperties dsProperties) {
    this.dsProperties = dsProperties;
  }

  @Override
  protected DataSource determineTargetDataSource() {
    return super.determineTargetDataSource();
  }

  @Override
  protected Object determineCurrentLookupKey() {
    TenantContext context = TenantContextHolder.getContext();
    if (context != null && context.getTenant() != null) {
      return context.getTenant().getDataSourceConfig().getId();
    }
    return null;
  }
}
