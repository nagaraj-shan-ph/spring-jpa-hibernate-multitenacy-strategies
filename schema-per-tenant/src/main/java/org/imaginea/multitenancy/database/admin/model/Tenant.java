package org.imaginea.multitenancy.database.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.imaginea.multitenancy.database.BaseEntity;
import org.imaginea.multitenancy.database.lms.TenantSchemaResolver;

/**
 * This JPA entity represents the <tt>accounts</tt> table in the <tt>masterdb</tt> database. This
 * table holds the details of the tenant schema databases.
 *
 * @version 1.0
 */
@Entity
@Table(name = "tenants", schema = TenantSchemaResolver.DEFAULT_SCHEMA)
public class Tenant extends BaseEntity {

  @Size(max = 100)
  @Column(name = "tenant_name")
  private String name;

  @Size(max = 256)
  @Column(name = "url")
  private String url;

  @Size(max = 256)
  @Column(name = "schema_name")
  @JsonIgnore
  private String schemaName;

  /** Name of the tenant to which the user belongs */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "datasource_config_id")
  @JsonIgnore
  private DataSourceConfig dataSourceConfig;

  /**
   * Gets tenant name.
   *
   * @return the tenant name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets tenant name.
   *
   * @param name the tenant name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets url.
   *
   * @param url the url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public DataSourceConfig getDataSourceConfig() {
    return dataSourceConfig;
  }

  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    this.dataSourceConfig = dataSourceConfig;
  }

  @Override
  public String toString() {
    return "Tenant{" + "name='" + name + '\'' + ", url='" + url + '\'' + ", schemaName='" + schemaName + '\'' + ", dataSourceConfig=" + dataSourceConfig + '}';
  }
}
