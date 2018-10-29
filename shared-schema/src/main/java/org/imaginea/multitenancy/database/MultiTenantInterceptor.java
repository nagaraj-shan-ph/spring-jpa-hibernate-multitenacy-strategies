package org.imaginea.multitenancy.database;

import java.io.Serializable;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiTenantInterceptor extends EmptyInterceptor {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (entity instanceof TenantEntity) {
      log.debug("[save] Updating the entity " + id + " with util information: " + TenantContextHolder.getContext().getTenant());
      ((TenantEntity) entity).setTenant(TenantContextHolder.getContext().getTenant());
      int i = ArrayUtils.indexOf(propertyNames, "tenant");
      state[i] = TenantContextHolder.getContext().getTenant();
    }
    return false;
  }

  @Override
  public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (entity instanceof TenantEntity) {
      log.debug("[delete] Updating the entity " + id + " with util information: " + TenantContextHolder.getContext().getTenant());
      ((TenantEntity) entity).setTenant(TenantContextHolder.getContext().getTenant());
    }
  }

  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
    if (entity instanceof TenantEntity) {
      log.debug("[flush-dirty] Updating the entity " + id + " with util information: " + TenantContextHolder.getContext().getTenant());
      ((TenantEntity) entity).setTenant(TenantContextHolder.getContext().getTenant());
    }
    return false;
  }

}
