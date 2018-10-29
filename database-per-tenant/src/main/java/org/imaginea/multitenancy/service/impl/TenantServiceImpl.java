package org.imaginea.multitenancy.service.impl;

import org.imaginea.multitenancy.database.admin.model.Tenant;
import org.imaginea.multitenancy.database.admin.repository.TenantRepository;
import org.imaginea.multitenancy.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService {

  @Autowired
  private TenantRepository tenantRepository;

  @Override
  public Tenant findByTenantName(String name) {
    return tenantRepository.findByName(name).orElse(null);
  }
}
