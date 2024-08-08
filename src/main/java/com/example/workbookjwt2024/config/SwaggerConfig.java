package com.example.workbookjwt2024.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                // security 설정
                .securitySchemes(List.of(apiKey()))             // 스웨거에서 사용할 API 인증 방식 목록
                .securityContexts(List.of(securityContext()))   // API 작업에 사용할 기본 인증 방식 목록
                .apiInfo(apiInfo());
    }

    private ApiKey apiKey(){
        return new ApiKey("Authorization", "Bearer Token", "header");
    }
    private SecurityContext securityContext(){
        // /api/ 로 시작하는 경로들에 대해서 Authorization 헤더를 지정하도록 설정
        return SecurityContext.builder().securityReferences(defaultAuth())
                .operationSelector(selector -> selector.requestMappingPattern().startsWith("/api/")).build();
    }
    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "global access");

        return List.of(new SecurityReference("Authorization", new AuthorizationScope[]{authorizationScope}));
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Boot Project Swagger")
                .build();
    }
}
