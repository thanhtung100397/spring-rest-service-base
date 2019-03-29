package com.spring.baseproject.configs.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.spring.baseproject.components.base.JSONProcessor;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseMessageReader;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseModelProvider;
import com.spring.baseproject.components.swagger.SwaggerApiGroupBuilder;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.utils.base.PackageScannerUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.plugins.Docket;
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
    @Value("${application.application.modules-package.name:modules}")
    private String rootModulePackageName;

    @Value("${server.port:8080}")
    private int serverPort;
    @Value("${application.swagger.excluded-modules}")
    private Set<String> swaggerExcludedModules;

    @Autowired
    private JSONProcessor jsonProcessor;
    @Autowired(required = false)
    private BuildProperties buildProperties;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SwaggerApiGroupBuilder swaggerApiGroupBuilder;

    @Bean
    public ApiInfo projectApiInfo() throws IOException {
        String version = buildProperties == null ? "dev" : buildProperties.getVersion();
        Map<String, String> apiInfo = jsonProcessor.parseFromResourceFiles(swaggerInfoPath);
        return swaggerApiGroupBuilder.newApiInfo(apiInfo.get("title"), apiInfo.get("description"),
                apiInfo.get("licenseUrl"), version, apiInfo.get("developBy"),
                apiInfo.get("contactEmail"));
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

    @PostConstruct
    public void init() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        String modulesPackageName = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        List<String> moduleNames = PackageScannerUtils.listAllSubPackages(modulesPackageName);
        for (String moduleName : moduleNames) {
            if (!swaggerExcludedModules.contains(moduleName)) {
                Docket moduleApiGroup = swaggerApiGroupBuilder.newSwaggerApiGroup(moduleName, modulesPackageName + "." + moduleName + ".controllers");
                configurableBeanFactory.registerSingleton("swaggerApiGroup" + moduleName, moduleApiGroup);
            }
        }
    }
}
