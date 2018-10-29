package org.imaginea.multitenancy.database;

import java.io.Serializable;

public interface TenantAware {

  Serializable getTenantId();
}
