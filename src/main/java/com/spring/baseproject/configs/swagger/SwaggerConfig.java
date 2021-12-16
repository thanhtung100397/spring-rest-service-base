package com.spring.baseproject.configs.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.spring.baseproject.components.base.JSONProcessor;
import com.spring.baseproject.components.swagger.*;
import com.spring.baseproject.constants.ApplicationConstants;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.operation.OperationAuthReader;
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider;
import springfox.documentation.swagger.readers.operation.SwaggerResponseMessageReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${application.swagger.info.path}")
    private String swaggerInfoPath;
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;
    @Value("${application.swagger.excluded-modules}")
    private Set<String> swaggerExcludedModules;
    @Value("${application.modules-package.modules}")
    private Set<String> allModules;

    @Autowired
    private JSONProcessor jsonProcessor;
    @Autowired(required = false)
    private BuildProperties buildProperties;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SwaggerApiGroupBuilder swaggerApiGroupBuilder;

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
    public ApiInfo projectApiInfo() throws IOException {
        String version = buildProperties == null ? "dev" : buildProperties.getVersion();
        Map<String, String> apiInfo = jsonProcessor.parseFromResourceFiles(swaggerInfoPath);
        return swaggerApiGroupBuilder.newApiInfo(apiInfo.get("title"), apiInfo.get("description"),
                apiInfo.get("licenseUrl"), version, apiInfo.get("developBy"),
                apiInfo.get("contactEmail"));
    }

    @PostConstruct
    public void init() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        String modulesPackageName = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        for (String moduleName : allModules) {
            if (!swaggerExcludedModules.contains(moduleName)) {
                Docket moduleApiGroup = swaggerApiGroupBuilder.newSwaggerApiGroup(moduleName, modulesPackageName + "." + moduleName + ".controllers");
                configurableBeanFactory.registerSingleton("swaggerApiGroup" + moduleName, moduleApiGroup);
            }
        }
    }
}
