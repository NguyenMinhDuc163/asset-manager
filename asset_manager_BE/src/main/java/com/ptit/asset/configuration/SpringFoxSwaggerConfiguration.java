package com.ptit.asset.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;



/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Configuration
//@EnableSwagger2
public class SpringFoxSwaggerConfiguration {

    @Bean
    public Docket api() {
        String groupName = "Swagger";
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build().groupName(groupName)
                .apiInfo(apiInfo());
    }

    private final String CLIENT_ID = "duykhanh";
    private final String CLIENT_SECRET = "PtitDev123!@#";
    private final String AUTH_SERVER = "abc";
//    @Bean
//    public Docket api() {
//        String groupName = "Swagger";
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
////                .apis(RequestHandlerSelectors.basePackage("com.ptit.asset.web.rest"))
//                .paths(PathSelectors.ant("/api/v1/*"))
//                .build().groupName(groupName)
//                .apiInfo(apiInfo())
//                .securitySchemes(Arrays.asList(securityScheme()))
//                .securityContexts(Arrays.asList(securityContext()));
//    }
    // http://localhost:8080/abc/authorize?
    // response_type=code
    // &client_id=gtopia
    // &redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fwebjars%2Fspringfox-swagger-ui%2Foauth2-redirect.html
    // &state=V2VkIEp1bCAxNSAyMDIwIDE2OjM3OjMxIEdNVCswNzAwIChJbmRvY2hpbmEgVGltZSk%3D√
    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/token", "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(AUTH_SERVER + "/authorize", CLIENT_ID, CLIENT_SECRET))
                .build();

        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(
                    Arrays.asList(new SecurityReference("spring_oauth", scopes())))
            .forPaths(PathSelectors.regex("/api/v1/*"))
            .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
//                .scopeSeparator(" ")
            .useBasicAuthenticationWithAccessCodeGrant(true)
            .build();
    }



    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                new AuthorizationScope("foo", "Access foo API") };
        return scopes;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Asset Management API",
                "Asset Management System API",
                "API TOS",
                "Terms of service",
                new Contact("Duy Khanh", "gtopia.vn", "voduykhanhnc@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }


}
