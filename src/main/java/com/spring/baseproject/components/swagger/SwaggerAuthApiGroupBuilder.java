package com.spring.baseproject.components.swagger;

import com.spring.baseproject.components.auth.AuthApiFilter;
import com.spring.baseproject.utils.auth.RouteScannerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwaggerAuthApiGroupBuilder extends SwaggerApiGroupBuilder {
    public static final Logger logger = LoggerFactory.getLogger(SwaggerAuthApiGroupBuilder.class);

    private String basePackageName;
    private String rootModulePackageName;
    private RouteScannerUtils.ApiFilter apiFilter;

    public SwaggerAuthApiGroupBuilder(String basePackageName, String rootModulePackageName,
                                      RouteScannerUtils.ApiFilter apiFilter) {
        this.basePackageName = basePackageName;
        this.rootModulePackageName = rootModulePackageName;
        this.apiFilter = apiFilter;
    }

    public SwaggerAuthApiGroupBuilder(String basePackageName, String rootModulePackageName) {
        this.basePackageName = basePackageName;
        this.rootModulePackageName = rootModulePackageName;
        this.apiFilter = new AuthApiFilter();
    }

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
        String rootModulePackage = basePackageName + "." + rootModulePackageName;
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
