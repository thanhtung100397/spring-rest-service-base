package com.spring.baseproject.components.auth;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.swagger.readers.operation.OperationAuthReader;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;

public class CustomSwaggerOperationAuthReader extends OperationAuthReader {
    private static final Logger LOG = LoggerFactory.getLogger(CustomSwaggerOperationAuthReader.class);

    @Override
    public void apply(OperationContext context) {
        String fullRequestPath = context.httpMethod().name() + " " + context.requestMappingPattern();

        Optional<SecurityContext> securityContext = Iterables
                .tryFind(context.getDocumentationContext().getSecurityContexts(),
                        input -> input.securityForPath(fullRequestPath) != null);
        List<SecurityReference> securityReferences = newArrayList();

        if (securityContext.isPresent()) {
            securityReferences = securityContext.get().securityForPath(fullRequestPath);
        }

        Optional<ApiOperation> apiOperationAnnotation = context.findAnnotation(ApiOperation.class);

        if (apiOperationAnnotation.isPresent() && null != apiOperationAnnotation.get().authorizations()) {
            Authorization[] authorizationAnnotations = apiOperationAnnotation.get().authorizations();
            if (authorizationAnnotations != null
                    && authorizationAnnotations.length > 0
                    && StringUtils.hasText(authorizationAnnotations[0].value())) {

                securityReferences = newArrayList();
                for (Authorization authorization : authorizationAnnotations) {
                    String value = authorization.value();
                    AuthorizationScope[] scopes = authorization.scopes();
                    List<springfox.documentation.service.AuthorizationScope> authorizationScopeList = newArrayList();
                    for (AuthorizationScope authorizationScope : scopes) {
                        String description = authorizationScope.description();
                        String scope = authorizationScope.scope();
                        if (!isNullOrEmpty(scope)) {
                            authorizationScopeList.add(
                                    new AuthorizationScopeBuilder()
                                            .scope(scope)
                                            .description(description)
                                            .build());
                        }
                    }
                    springfox.documentation.service.AuthorizationScope[] authorizationScopes = authorizationScopeList
                            .toArray(new springfox.documentation.service.AuthorizationScope[authorizationScopeList.size()]);
                    SecurityReference securityReference =
                            SecurityReference.builder()
                                    .reference(value)
                                    .scopes(authorizationScopes)
                                    .build();
                    securityReferences.add(securityReference);
                }
            }
        }
        if (securityReferences != null) {
            LOG.debug("Authorization count {} for method {}", securityReferences.size(), context.getName());
            context.operationBuilder().authorizations(securityReferences);
        }
    }
}
