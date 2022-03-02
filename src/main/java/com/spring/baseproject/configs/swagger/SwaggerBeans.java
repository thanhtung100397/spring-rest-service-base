package com.spring.baseproject.configs.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.spring.baseproject.components.base.JSONProcessor;
import com.spring.baseproject.components.swagger.*;
import com.spring.baseproject.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.operation.OperationAuthReader;
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider;
import springfox.documentation.swagger.readers.operation.SwaggerResponseMessageReader;

import java.io.IOException;
import java.util.Map;

@Configuration
public class SwaggerBeans {
    @Value("${application.swagger.info.path}")
    private String swaggerInfoPath;
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;

    private final JSONProcessor jsonProcessor;
    private final BuildProperties buildProperties;

    public SwaggerBeans(JSONProcessor jsonProcessor,
                        BuildProperties buildProperties) {
        this.jsonProcessor = jsonProcessor;
        this.buildProperties = buildProperties;
    }

    @Bean
    public SwaggerApiGroupBuilder swaggerApiGroupBuilder() {
        return new SwaggerAuthApiGroupBuilder(ApplicationConstants.BASE_PACKAGE_NAME, rootModulePackageName);
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    public SwaggerResponseMessageReader swaggerResponseMessageReader(TypeNameExtractor typeNameExtractor,
                                                                     TypeResolver typeResolver) {
        return new CustomSwaggerResponseMessageReader(typeNameExtractor, typeResolver);
    }

    @Bean
    public OperationAuthReader operationAuthReader() {
        return new CustomSwaggerOperationAuthReader();
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    public SwaggerOperationModelsProvider swaggerOperationModelsProvider(TypeResolver typeResolver) {
        return new CustomSwaggerResponseModelProvider(typeResolver);
    }

    @Bean
    public ApiInfo projectApiInfo(SwaggerApiGroupBuilder swaggerApiGroupBuilder) throws IOException {
        String version = buildProperties == null ? "dev" : buildProperties.getVersion();
        Map<String, String> apiInfo = jsonProcessor.parseFromResourceFiles(swaggerInfoPath);
        return swaggerApiGroupBuilder.newApiInfo(apiInfo.get("title"), apiInfo.get("description"),
                                                 apiInfo.get("licenseUrl"), version, apiInfo.get("developBy"),
                                                 apiInfo.get("contactEmail"));
    }
}
