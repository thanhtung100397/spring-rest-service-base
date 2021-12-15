package com.spring.baseproject.components.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;
import org.springframework.lang.Nullable;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.readers.operation.SwaggerResponseMessageReader;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spi.schema.contexts.ModelContext.returnValue;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.httpStatusCode;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.message;
import static springfox.documentation.swagger.readers.operation.ResponseHeaders.headers;
import static springfox.documentation.swagger.readers.operation.ResponseHeaders.responseHeaders;

public class CustomSwaggerResponseMessageReader extends SwaggerResponseMessageReader {
    private final TypeNameExtractor typeNameExtractor;
    private final TypeResolver typeResolver;

    public CustomSwaggerResponseMessageReader(TypeNameExtractor typeNameExtractor, TypeResolver typeResolver) {
        super(typeNameExtractor, typeResolver);
        this.typeNameExtractor = typeNameExtractor;
        this.typeResolver = typeResolver;
    }

    @Override
    protected Set<ResponseMessage> read(OperationContext context) {
        ResolvedType defaultResponse = context.getReturnType();
        Optional<ApiOperation> operationAnnotation = context.findAnnotation(ApiOperation.class);
        Optional<ResolvedType> operationResponse =
                operationAnnotation.transform(new Function<ApiOperation, ResolvedType>() {
                    @Nullable
                    @Override
                    public ResolvedType apply(@Nullable ApiOperation input) {
                        return resolvedType(defaultResponse, input);
                    }
                });
        Optional<ResponseHeader[]> defaultResponseHeaders = operationAnnotation.transform(responseHeaders());
        Map<String, Header> defaultHeaders = newHashMap();
        if (defaultResponseHeaders.isPresent()) {
            defaultHeaders.putAll(headers(defaultResponseHeaders.get()));
        }

        List<Responses> allApiResponses = context.findAllAnnotations(Responses.class);
        Set<ResponseMessage> responseMessages = newHashSet();

        for (Responses apiResponses : allApiResponses) {
            Response[] apiResponseAnnotations = apiResponses.value();
            boolean hasSuccessful = false;
            for (Response response : apiResponseAnnotations) {
                ResponseValue responseValue = response.responseValue();
                if (responseValue != null) {
                    ApiResponse apiResponse = convertResponse(responseValue.specialCode(),
                            responseValue.message(),
                            response.responseBody());
                    ModelContext modelContext = returnValue(
                            context.getGroupName(), apiResponse.response(),
                            context.getDocumentationType(),
                            context.getAlternateTypeProvider(),
                            context.getGenericsNamingStrategy(),
                            context.getIgnorableParameterTypes());
                    Optional<ModelReference> responseModel = Optional.absent();
                    Optional<ResolvedType> type = resolvedType(null, apiResponse);
                    if (!hasSuccessful && responseValue == ResponseValue.SUCCESS) {
                        hasSuccessful = true;
                        type = type.or(operationResponse);
                    }
                    if (type.isPresent()) {
                        responseModel = Optional.of(
                                modelRefFactory(modelContext, typeNameExtractor)
                                        .apply(context.alternateFor(type.get())));
                    }
                    Map<String, Header> headers = newHashMap(defaultHeaders);
                    headers.putAll(headers(apiResponse.responseHeaders()));

                    responseMessages.add(new ResponseMessageBuilder()
                            .code(apiResponse.code())
                            .message(apiResponse.message())
                            .responseModel(responseModel.orNull())
                            .headersWithDescription(headers)
                            .build());
                }
            }
        }
        if (operationResponse.isPresent()) {
            ModelContext modelContext = returnValue(
                    context.getGroupName(),
                    operationResponse.get(),
                    context.getDocumentationType(),
                    context.getAlternateTypeProvider(),
                    context.getGenericsNamingStrategy(),
                    context.getIgnorableParameterTypes());
            ResolvedType resolvedType = context.alternateFor(operationResponse.get());

            ModelReference responseModel = modelRefFactory(modelContext, typeNameExtractor).apply(resolvedType);
            context.operationBuilder().responseModel(responseModel);
            ResponseMessage defaultMessage = new ResponseMessageBuilder()
                    .code(httpStatusCode(context))
                    .message(message(context))
                    .responseModel(responseModel)
                    .build();
            if (!responseMessages.contains(defaultMessage) && !"void".equals(responseModel.getType())) {
                responseMessages.add(defaultMessage);
            }
        }
        return responseMessages;
    }

    private ResolvedType resolvedType(ResolvedType resolvedType, ApiOperation apiOperation) {
        return getResolvedType(apiOperation, typeResolver, resolvedType);
    }

    private Optional<ResolvedType> resolvedType(ResolvedType resolvedType, ApiResponse apiResponse) {
        return fromNullable(getResolvedType(apiResponse, typeResolver, resolvedType));
    }

    private ResolvedType getResolvedType(ApiOperation annotation, TypeResolver typeResolver, ResolvedType defaultType) {
        if (null != annotation) {
            if ("List".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(List.class, annotation.response());
            } else if ("Set".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(Set.class, annotation.response());
            } else if (defaultType.getErasedType() != Void.class) {
                return typeResolver.resolve(BaseResponseBody.class, defaultType);
            } else {
                return typeResolver.resolve(BaseResponseBody.class, Object.class);
            }
        }
        return defaultType;
    }

    private ResolvedType getResolvedType(ApiResponse annotation, TypeResolver typeResolver, ResolvedType defaultType) {
        if (null != annotation && Void.class != annotation.response()) {
            if ("List".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(List.class, annotation.response());
            } else if ("Set".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(Set.class, annotation.response());
            } else if (annotation.response() != Void.class) {
                return typeResolver.resolve(BaseResponseBody.class, annotation.response());
            } else {
                return typeResolver.resolve(BaseResponseBody.class, Object.class);
            }
        }
        return defaultType;
    }

    private ApiResponse convertResponse(int code, String message, Class<?> response) {
        return new ApiResponse() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ApiResponse.class;
            }

            @Override
            public int code() {
                return code;
            }

            @Override
            public String message() {
                return message;
            }

            @Override
            public Class<?> response() {
                Class<?> result = Void.class;
                if (response != null) {
                    result = response;
                }
                return result;
            }

            @Override
            public String reference() {
                return "";
            }

            @Override
            public ResponseHeader[] responseHeaders() {
                return new ResponseHeader[0];
            }

            @Override
            public String responseContainer() {
                return "";
            }
        };
    }
}
