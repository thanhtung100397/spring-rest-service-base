package com.spring.baseproject.components.swagger;

import com.spring.baseproject.constants.ResponseValue;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Component
public class SwaggerApiGroupBuilder {

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

    public Docket newSwaggerApiGroup(String groupName, String packageName) {
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

    public ApiInfo newApiInfo(String title, String description,
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
}
