package com.adwork.microservices.users.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    //@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(Predicates.not(PathSelectors.regex("/error")))
            .build()
            .apiInfo(metadata())
            .useDefaultResponseMessages(false)
            .securitySchemes(new ArrayList<>(Arrays.asList(new ApiKey("Bearer %token", "Authorization", "Header"))))
            .tags(new Tag("users", "Operations about users"))
            .tags(new Tag("ping", "Just a ping"))
            .genericModelSubstitutes(Optional.class);
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
            .title("User service api application")
            .description("API for user details and login/logout/register service")
            .version("0.0.1")
            .license("MIT License").licenseUrl("http:opensource.org/licenses/MIT")
            .build();
    }
}
