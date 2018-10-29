package org.imaginea.multitenancy.service;

import org.imaginea.multitenancy.database.admin.model.Tenant;

public interface TenantService {

  Tenant findByTenantName(String name);
}
