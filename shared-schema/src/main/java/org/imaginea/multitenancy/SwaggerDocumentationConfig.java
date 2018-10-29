package org.imaginea.multitenancy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Configuration class for building swagger-ui. */
@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {

  @Value("${application.basePath:/api/}")
  private String swaggerDocBasePath;

  /**
   * Api info api info.
   *
   * @return the api info
   */
  ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("MultiTenant Application").description("Schema per admin using postgres sql").license("Apache 2.0")
        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").termsOfServiceUrl("").version("1.0.0")
        .contact(new Contact("", "", "apiteam@imaginea.com")).build();
  }

  /**
   * Custom implementation docket.
   *
   * @return the docket
   */
  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("org.imaginea")).build().useDefaultResponseMessages(false)
        .pathProvider(new BasePathAwareRelativePathProvider(swaggerDocBasePath));
  }

  static class BasePathAwareRelativePathProvider extends AbstractPathProvider {

    private String basePath;

    public BasePathAwareRelativePathProvider(String basePath) {
      this.basePath = basePath;
    }

    @Override
    protected String applicationPath() {
      return basePath;
    }

    @Override
    protected String getDocumentationPath() {
      return "/";
    }

    @Override
    public String getOperationPath(String operationPath) {
      UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
      return Paths.removeAdjacentForwardSlashes(uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
    }
  }
}
