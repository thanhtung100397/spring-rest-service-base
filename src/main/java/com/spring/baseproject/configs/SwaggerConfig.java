package com.spring.baseproject.configs;

import com.fasterxml.classmate.TypeResolver;
import com.spring.baseproject.components.JSONProcessor;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseMessageReader;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseModelProvider;
import com.spring.baseproject.constants.ResponseValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider;
import springfox.documentation.swagger.readers.operation.SwaggerResponseMessageReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${application.base-package-name}")
    private String basePackageName;
    @Value("${application.swagger.info.path}")
    private String swaggerInfoPath;

    @Autowired
    private JSONProcessor jsonProcessor;
    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Bean
    public Docket demoApisGroup(ApiInfo projectApiInfo) {
        ArrayList<ResponseMessage> globalResponseMessage = new ArrayList<>();
        globalResponseMessage.add(new ResponseMessageBuilder()
                .code(ResponseValue.UNEXPECTED_ERROR_OCCURRED.specialCode())
                .message(ResponseValue.UNEXPECTED_ERROR_OCCURRED.message())
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(projectApiInfo)
                .groupName("demo")
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackageName + ".modules.demo"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalResponseMessage)
                .globalResponseMessage(RequestMethod.POST, globalResponseMessage)
                .globalResponseMessage(RequestMethod.PUT, globalResponseMessage)
                .globalResponseMessage(RequestMethod.DELETE, globalResponseMessage);

        //        Uncomment to use Swagger ui authorization/authentication
        //        .securitySchemes(Collections.singletonList(apiKey()))
        //        .securityContexts(Collections.singletonList(securityContext()));
    }

    @Bean
    public ApiInfo projectApiInfo() throws IOException {
        String version = buildProperties == null? "dev" : buildProperties.getVersion();
        Map<String, String> apiInfo = jsonProcessor.parseFromResourceFiles(swaggerInfoPath);
        return new ApiInfoBuilder().title(apiInfo.get("title"))
                .description(apiInfo.get("description"))
                .licenseUrl(apiInfo.get("licenseUrl"))
                .version(version)
                .build();
    }

    @Bean
    public SwaggerResponseMessageReader swaggerResponseMessageReader(TypeNameExtractor typeNameExtractor,
                                                                     TypeResolver typeResolver) {
        return new CustomSwaggerResponseMessageReader(typeNameExtractor, typeResolver);
    }

    @Bean
    public SwaggerOperationModelsProvider swaggerOperationModelsProvider(TypeResolver typeResolver) {
        return new CustomSwaggerResponseModelProvider(typeResolver);
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("apiKey",
                authorizationScopes));
    }
}
