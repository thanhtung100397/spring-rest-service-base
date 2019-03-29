package com.spring.baseproject.components.auth;

import com.spring.baseproject.components.swagger.SwaggerApiGroupBuilder;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.utils.auth.RouteScannerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Method;
import java.util.*;

@Component
@Primary
public class SwaggerAuthApiGroupBuilder extends SwaggerApiGroupBuilder {
    public static final Logger logger = LoggerFactory.getLogger(SwaggerAuthApiGroupBuilder.class);

    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;

    @Autowired
    private RouteScannerUtils.ApiFilter apiFilter;

    public Docket newSwaggerApiGroup(String groupName, String packageName) {
        return super.newSwaggerApiGroup(groupName, packageName)
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext(groupName)));

    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    private SecurityContext securityContext(String moduleName) {
        Set<String> moduleAuthorizationRequiredRoutes = scanAuthorizationRequiredRoutes(moduleName);
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(moduleAuthorizationRequiredRoutes::contains)
                .build();
    }

    public Set<String> scanAuthorizationRequiredRoutes(String moduleName) {
        Set<String> routes = new HashSet<>();
        String rootModulePackage = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        RouteScannerUtils.scanRoutes(rootModulePackage + "." + moduleName + ".controllers",
                null, null,
                apiFilter,
                new RouteScannerUtils.RouteFetched() {
                    @Override
                    public void onNewContainerClass(Class<?> containerClass) {

                    }

                    @Override
                    public void onNewMethod(Method method) {

                    }

                    @Override
                    public void onNewRouteFetched(RequestMethod method, String route) {
                        routes.add(HttpMethod.valueOf(method.name()) + " " + route);
                    }
                }, false);
        logger.info("module [" + moduleName + "] scanned - found " +
                routes.size() + " authorization required route(s)");
        return routes;
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
