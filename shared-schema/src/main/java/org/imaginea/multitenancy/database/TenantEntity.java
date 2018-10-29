package org.imaginea.multitenancy.database;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.imaginea.multitenancy.database.admin.model.Tenant;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The type Tenant entity.
 */
@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "long")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenant")
public abstract class TenantEntity extends BaseEntity {

  public static final String TENANT_COLUMN = "tenant_id";
  /**
   * Name of the tenant to which the user belongs
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = TENANT_COLUMN, updatable = false)
  @JsonIgnore
  private Tenant tenant;

  /**
   * Gets tenant.
   *
   * @return the tenant
   */
  public Tenant getTenant() {
    return tenant;
  }

  /**
   * Sets tenant.
   *
   * @param tenant the tenant
   */
  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  @PrePersist
  @PreUpdate
  @PreRemove
  public void onPrePersist() {
    this.tenant = TenantContextHolder.getContext().getTenant();
  }

}
