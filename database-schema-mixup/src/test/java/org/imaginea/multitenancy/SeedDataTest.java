package org.imaginea.multitenancy;


import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.imaginea.multitenancy.database.admin.model.DataSourceConfig;
import org.imaginea.multitenancy.database.admin.model.Tenant;
import org.imaginea.multitenancy.database.admin.repository.DataSourceConfigRepository;
import org.imaginea.multitenancy.database.admin.repository.TenantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SeedDataTest {

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  TenantRepository tenantRepository;

  @Autowired
  DataSourceConfigRepository dataSourceConfigRepository;

  @Test
  public void createSeed() {
    Tenant tenant = createTenant();
    System.out.println(tenant);
  }

  private Tenant createTenant() {
    Tenant tenant = new Tenant();
    tenant.setName("test");
    tenant.setUrl("https://admin.url.com/");
    tenant.setSchemaName("test");
    boolean database1 = dataSourceConfigRepository.findByName("Database1").isPresent();
    tenant.setDataSourceConfig(dataSourceConfigRepository.findByName("Database1").orElseGet(this::createDatasourceConfig));
    tenantRepository.save(tenant);
    return tenant;
  }

  private DataSourceConfig createDatasourceConfig() {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setName("Database1");
    dataSourceConfig.setDatabaseUrl("jdbc:postgresql://localhost:5432/database-schema-mixup");
    dataSourceConfig.setUsername("postgres");
    dataSourceConfig.setPassword("postgres");
    dataSourceConfig.setInitialize(true);
    dataSourceConfigRepository.save(dataSourceConfig);
    return dataSourceConfig;
  }

}
