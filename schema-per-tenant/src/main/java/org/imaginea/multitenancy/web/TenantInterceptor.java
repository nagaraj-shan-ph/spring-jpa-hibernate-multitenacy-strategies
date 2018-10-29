package org.imaginea.multitenancy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imaginea.multitenancy.database.TenantContextHolder;
import org.imaginea.multitenancy.database.admin.model.Tenant;
import org.imaginea.multitenancy.database.admin.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

  private static final String TENANT_HEADER = "X-TenantID";
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  TenantRepository repository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String tenantHeader = request.getHeader(TENANT_HEADER);
    boolean tenantSet = false;
    if (!tenantHeader.isEmpty()) {
      Tenant tenant = repository.findByName(tenantHeader).orElseThrow(() -> new RuntimeException("tenants not found"));
      logger.debug("Set TenantContextHolder: {}", tenant);
      TenantContextHolder.setTenant(tenant, true);
      tenantSet = true;
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"error\": \"No admin supplied\"}");
      response.getWriter().flush();
    }
    return tenantSet;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    logger.debug("Clear TenantContextHolder: {}", TenantContextHolder.getContext());
    TenantContextHolder.reset();
  }
}
