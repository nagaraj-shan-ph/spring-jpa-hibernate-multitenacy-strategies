package org.imaginea.multitenancy.database.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.imaginea.multitenancy.model.BaseEntity;
import org.imaginea.multitenancy.database.lms.TenantSchemaResolver;

/** Defines the multi-tenant datasource configuration. */
@Entity
@Table(name = "data_source_configs", schema = TenantSchemaResolver.DEFAULT_SCHEMA)
public class DataSourceConfig extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "database_url")
  private String databaseUrl;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "initialize")
  private boolean initialize;

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets databaseUrl.
   *
   * @return the databaseUrl
   */
  public String getDatabaseUrl() {
    return databaseUrl;
  }

  /**
   * Sets databaseUrl.
   *
   * @param databaseUrl the databaseUrl
   */
  public void setDatabaseUrl(String databaseUrl) {
    this.databaseUrl = databaseUrl;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Is initialize boolean.
   *
   * @return the boolean
   */
  public boolean isInitialize() {
    return initialize;
  }

  /**
   * Sets initialize.
   *
   * @param initialize the initialize
   */
  public void setInitialize(boolean initialize) {
    this.initialize = initialize;
  }
}
