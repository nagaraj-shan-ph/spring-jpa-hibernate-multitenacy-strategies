package org.imaginea.multitenancy.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.imaginea.multitenancy.database")
public class JpaConfig {

}
