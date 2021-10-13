package com.ezfarm.ezfarmback.config;

import com.ezfarm.ezfarmback.security.CurrentUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo("ezfarm", "V1"))
        .groupName("V1")
        .securityContexts(Collections.singletonList(securityContext()))
        .securitySchemes(Collections.singletonList(apiKey()))
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.ezfarm.ezfarmback"))
        .paths(PathSelectors.ant("/api/**"))
        .build()
        .ignoredParameterTypes(CurrentUser.class)
        .useDefaultResponseMessages(false);
  }

  private ApiInfo apiInfo(String title, String version) {
    return new ApiInfo(
        title,
        "Ezfarm API Docs",
        version,
        "www.test.com",
        new Contact("test", "www.test.com", "test@email.com"),
        "license",
        "licenseUrl",
        new ArrayList<>()
    );
  }

  private ApiKey apiKey() {
    return new ApiKey("Authorization", "Authorization", "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global",
        "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections
        .singletonList(new SecurityReference("Authorization", authorizationScopes));
  }
}
