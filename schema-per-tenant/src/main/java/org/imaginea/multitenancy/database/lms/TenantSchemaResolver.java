package org.imaginea.multitenancy.database.lms;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.imaginea.multitenancy.database.TenantContext;
import org.imaginea.multitenancy.database.TenantContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

  public static final String DEFAULT_SCHEMA = "lms";

  @Override
  public String resolveCurrentTenantIdentifier() {
    TenantContext context = TenantContextHolder.getContext();
    if (context != null && context.getTenant() != null && StringUtils.isNotBlank(context.getTenant().getSchemaName())) {
      return context.getTenant().getSchemaName();
    } else {
      return DEFAULT_SCHEMA;
    }
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
