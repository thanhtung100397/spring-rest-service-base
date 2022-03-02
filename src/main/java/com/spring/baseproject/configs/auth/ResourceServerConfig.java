package com.spring.baseproject.configs.auth;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.annotations.rbac.RoleBaseAccessControl;
import com.spring.baseproject.components.rbac.InMemoryRoutesDictionary;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.modules.auth.services.IAuthorization;
import com.spring.baseproject.utils.auth.RouteScannerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public static final Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);

    @Value("${application.oauth2.resource-server.id}")
    private String resourceID;
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;
    @Value("${application.modules-package.modules}")
    private Set<String> allModules;

    private final DefaultTokenServices tokenService;
    private final TokenStore tokenStore;
    private final IAuthorization authorization;
    private final InMemoryRoutesDictionary inMemoryRoutesDictionary;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;

    public ResourceServerConfig(DefaultTokenServices tokenService, TokenStore tokenStore,
                                IAuthorization authorization, InMemoryRoutesDictionary inMemoryRoutesDictionary,
                                AuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.tokenService = tokenService;
        this.tokenStore = tokenStore;
        this.authorization = authorization;
        this.inMemoryRoutesDictionary = inMemoryRoutesDictionary;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        NoAuthorizationRequiredRoutes noAuthorizationRequiredRoutes = createNoAuthorizationRequiredRoutes();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
                http.cors().and()
                        .csrf().disable()
                        .authorizeRequests();
        for (Map.Entry<HttpMethod, Set<String>> apiEntry : noAuthorizationRequiredRoutes.getApis().entrySet()) {
            expressionInterceptUrlRegistry = expressionInterceptUrlRegistry
                    .antMatchers(apiEntry.getKey(), apiEntry.getValue().toArray(new String[0]))
                    .permitAll();
        }
        expressionInterceptUrlRegistry.anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JWTAuthorizationFilter(authorization, inMemoryRoutesDictionary),
                        RequestCacheAwareFilter.class);
    }

    public NoAuthorizationRequiredRoutes createNoAuthorizationRequiredRoutes() {
        logger.info("Start scanning no authorization required routes...");
        NoAuthorizationRequiredRoutes noAuthorizationRequiredRoutes = new NoAuthorizationRequiredRoutes();
        String rootModulePackage = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        Set<Class<? extends Annotation>> excludeAnnotations = new HashSet<>();
        excludeAnnotations.add(AuthorizationRequired.class);
        excludeAnnotations.add(RoleBaseAccessControl.class);
        for (String moduleName : allModules) {
            int apiFound = 0;
            RouteScannerUtils.scanRoutes(rootModulePackage + "." + moduleName + ".controllers",
                    null, excludeAnnotations,
                    (containClass, method) -> method.getDeclaredAnnotation(AuthorizationRequired.class) == null &&
                            method.getDeclaredAnnotation(RoleBaseAccessControl.class) == null,
                    new RouteScannerUtils.RouteFetched() {
                        @Override
                        public void onNewContainerClass(Class<?> containerClass) {

                        }

                        @Override
                        public void onNewMethod(Method method) {

                        }

                        @Override
                        public void onNewRouteFetched(RequestMethod method, String route) {
                            noAuthorizationRequiredRoutes.addApi(HttpMethod.valueOf(method.name()), route);
                        }
                    }, true);
            logger.info("module [" + moduleName + "] scanned - found " +
                    apiFound + " no authorization required route(s)");
        }
        logger.info("Scanning no authorization required routes...DONE");
        return noAuthorizationRequiredRoutes;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceID)
                .tokenServices(tokenService)
                .tokenStore(tokenStore);
    }
}
