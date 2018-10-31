package org.imaginea.multitenancy.database.admin.repository;

import java.util.Optional;
import org.imaginea.multitenancy.database.admin.model.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, Long> {

  Optional<DataSourceConfig> findByName(String name);
}
