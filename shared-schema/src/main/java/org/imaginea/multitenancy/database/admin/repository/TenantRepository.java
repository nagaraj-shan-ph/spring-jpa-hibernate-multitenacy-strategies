package org.imaginea.multitenancy.database.admin.repository;

import java.util.Optional;

import org.imaginea.multitenancy.database.admin.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the {@link Tenant} JPA entity. Any custom methods, not already defined in
 * {@link JpaRepository}, are to be defined here.
 */
public interface TenantRepository extends JpaRepository<Tenant, Long> {

  Optional<Tenant> findByName(String name);
}
