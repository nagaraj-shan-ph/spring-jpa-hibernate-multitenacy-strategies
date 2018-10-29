package org.imaginea.multitenancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SbootMultitenancyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SbootMultitenancyApplication.class, args);
  }

}
