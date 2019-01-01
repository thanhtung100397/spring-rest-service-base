package com.spring.baseproject.components.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.swagger.annotations.Annotations.resolvedTypeFromOperation;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class CustomSwaggerResponseModelProvider extends SwaggerOperationModelsProvider {
    private final TypeResolver typeResolver;

    @Autowired
    public CustomSwaggerResponseModelProvider(TypeResolver typeResolver) {
        super(typeResolver);
        this.typeResolver = typeResolver;
    }

    @Override
    public void apply(RequestMappingContext context) {
        collectFromApiOperation(context);
        collectApiResponses(context);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }

    private void collectFromApiOperation(RequestMappingContext context) {
        ResolvedType returnType = context.getReturnType();
        returnType = context.alternateFor(returnType);
        Optional<ResolvedType> returnParameter = context.findAnnotation(ApiOperation.class)
                .transform(resolvedTypeFromOperation(typeResolver, returnType));
        if (returnParameter.isPresent() && returnParameter.get() != returnType) {
            context.operationModelsBuilder().addReturn(returnParameter.get());
        }
    }

    private void collectApiResponses(RequestMappingContext context) {
        List<Responses> allApiResponses = context.findAnnotations(Responses.class);
        Set<ResolvedType> seenTypes = newHashSet();
        for (Responses responses : allApiResponses) {
            List<ResolvedType> modelTypes = toResolvedTypes(context).apply(responses);
            for (ResolvedType modelType : modelTypes) {
                if (!seenTypes.contains(modelType)) {
                    seenTypes.add(modelType);
                    context.operationModelsBuilder().addReturn(modelType);
                }
            }
        }
    }

    private Function<Responses, List<ResolvedType>> toResolvedTypes(final RequestMappingContext context) {
        return input -> {
            List<ResolvedType> resolvedTypes = newArrayList();
            for (Response response : input.value()) {
                ResolvedType modelType = context.alternateFor(typeResolver.resolve(response.responseBody()));
                resolvedTypes.add(modelType);
            }
            return resolvedTypes;
        };
    }
}
