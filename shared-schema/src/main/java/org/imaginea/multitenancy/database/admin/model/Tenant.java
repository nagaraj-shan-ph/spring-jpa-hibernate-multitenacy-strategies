package org.imaginea.multitenancy.database.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.imaginea.multitenancy.database.BaseEntity;

/**
 * This JPA entity represents the <tt>accounts</tt> table in the <tt>masterdb</tt> database. This
 * table holds the details of the admin schema databases.
 *
 * @version 1.0
 */
@Entity
@Table(name = "tenants")
public class Tenant extends BaseEntity {

  @Size(max = 100)
  @Column(name = "tenant_name", unique = true)
  private String name;

  @Size(max = 256)
  @Column(name = "url")
  private String url;

  @Size(max = 256)
  @Column(name = "schema_name", unique = true)
  @JsonIgnore
  private String schemaName;

  /**
   * Gets admin name.
   *
   * @return the admin name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets admin name.
   *
   * @param name the admin name
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
}
