package com.spring.baseproject.configs.swagger;

import com.spring.baseproject.components.swagger.*;
import com.spring.baseproject.constants.ApplicationConstants;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;
    @Value("${application.swagger.excluded-modules}")
    private Set<String> swaggerExcludedModules;
    @Value("${application.modules-package.modules}")
    private Set<String> allModules;

    private final BeanFactory beanFactory;
    private final SwaggerApiGroupBuilder swaggerApiGroupBuilder;

    public SwaggerConfig(BeanFactory beanFactory, SwaggerApiGroupBuilder swaggerApiGroupBuilder) {
        this.beanFactory = beanFactory;
        this.swaggerApiGroupBuilder = swaggerApiGroupBuilder;
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
