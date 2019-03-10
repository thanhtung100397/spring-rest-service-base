package com.spring.baseproject.configs;

import com.fasterxml.classmate.TypeResolver;
import com.spring.baseproject.components.JSONProcessor;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseMessageReader;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseModelProvider;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.utils.PackageScannerUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider;
import springfox.documentation.swagger.readers.operation.SwaggerResponseMessageReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${application.swagger.info.path}")
    private String swaggerInfoPath;
    @Value("${application.application.modules-package.name:modules}")
    private String modulePackageName;

    @Autowired
    private JSONProcessor jsonProcessor;
    @Autowired(required = false)
    private BuildProperties buildProperties;
    @Autowired
    private BeanFactory beanFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public List<ResponseMessage> globalResponseMessages_GET() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.UNEXPECTED_ERROR_OCCURRED.specialCode())
                .message(ResponseValue.UNEXPECTED_ERROR_OCCURRED.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.MISSING_REQUEST_PARAMS.specialCode())
                .message(ResponseValue.MISSING_REQUEST_PARAMS.message())
                .build());
        return responseMessages;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public List<ResponseMessage> globalResponseMessages_POST() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.UNEXPECTED_ERROR_OCCURRED.specialCode())
                .message(ResponseValue.UNEXPECTED_ERROR_OCCURRED.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY.specialCode())
                .message(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.FIELD_VALIDATION_ERROR.specialCode())
                .message(ResponseValue.FIELD_VALIDATION_ERROR.message())
                .build());
        return responseMessages;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public List<ResponseMessage> globalResponseMessages_PUT() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.UNEXPECTED_ERROR_OCCURRED.specialCode())
                .message(ResponseValue.UNEXPECTED_ERROR_OCCURRED.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY.specialCode())
                .message(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.FIELD_VALIDATION_ERROR.specialCode())
                .message(ResponseValue.FIELD_VALIDATION_ERROR.message())
                .build());
        return responseMessages;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public List<ResponseMessage> globalResponseMessages_DELETE() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.UNEXPECTED_ERROR_OCCURRED.specialCode())
                .message(ResponseValue.UNEXPECTED_ERROR_OCCURRED.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY.specialCode())
                .message(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY.message())
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(ResponseValue.FIELD_VALIDATION_ERROR.specialCode())
                .message(ResponseValue.FIELD_VALIDATION_ERROR.message())
                .build());
        return responseMessages;
    }

    @Bean
    public ApiInfo projectApiInfo() throws IOException {
        String version = buildProperties == null ? "dev" : buildProperties.getVersion();
        Map<String, String> apiInfo = jsonProcessor.parseFromResourceFiles(swaggerInfoPath);
        return newApiInfo(apiInfo.get("title"), apiInfo.get("description"),
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
        String modulesPackageName = ApplicationConstants.BASE_PACKAGE_NAME+"."+modulePackageName;
        List<String> moduleNames = PackageScannerUtils.listAllSubPackages(modulesPackageName);
        for (String moduleName : moduleNames) {
            Docket moduleApiGroup = newSwaggerApiGroup(moduleName, modulesPackageName + "." + moduleName + ".controllers");
            configurableBeanFactory.registerSingleton("swaggerApiGroup" + moduleName, moduleApiGroup);
        }
    }

    private Docket newSwaggerApiGroup(String groupName, String packageName) {
        String moduleTitle = groupName.toUpperCase().replace("_", " ");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(newApiInfo(moduleTitle, null, null, null, null, null))
                .select()
                .apis(RequestHandlerSelectors.basePackage(packageName))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalResponseMessages_GET())
                .globalResponseMessage(RequestMethod.POST, globalResponseMessages_POST())
                .globalResponseMessage(RequestMethod.PUT, globalResponseMessages_PUT())
                .globalResponseMessage(RequestMethod.DELETE, globalResponseMessages_DELETE());
    }

    private ApiInfo newApiInfo(String title, String description,
                               String licenseUrl, String version,
                               String developBy, String contactEmail) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .licenseUrl(licenseUrl)
                .version(version)
                .contact(new Contact(developBy, null, contactEmail))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
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
