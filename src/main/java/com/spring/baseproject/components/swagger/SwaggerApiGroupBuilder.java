package com.spring.baseproject.components.swagger;

import com.spring.baseproject.constants.ResponseValue;
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

public class SwaggerApiGroupBuilder {

    private List<ResponseMessage> newResponseMessages(ResponseValue... responseValues) {
        List<ResponseMessage> results = new ArrayList<>();
        for (ResponseValue responseValue : responseValues) {
            results.add(new ResponseMessageBuilder()
                    .code(responseValue.specialCode())
                    .message(responseValue.message())
                    .build());
        }
        return results;
    }

    public List<ResponseMessage> globalResponseMessages_GET() {
        return newResponseMessages(ResponseValue.MISSING_REQUEST_PARAMS,
                ResponseValue.INVALID_REQUEST_PARAMS,
                ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

    public List<ResponseMessage> globalResponseMessages_POST() {
        return newResponseMessages(ResponseValue.INVALID_FIELDS,
                ResponseValue.INVALID_OR_MISSING_REQUEST_BODY,
                ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

    public List<ResponseMessage> globalResponseMessages_PUT() {
        return newResponseMessages(ResponseValue.INVALID_FIELDS,
                ResponseValue.INVALID_OR_MISSING_REQUEST_BODY,
                ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

    public List<ResponseMessage> globalResponseMessages_DELETE() {
        return newResponseMessages(ResponseValue.INVALID_FIELDS,
                ResponseValue.INVALID_OR_MISSING_REQUEST_BODY,
                ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

    public Docket newSwaggerApiGroup(String groupName, String packageName) {
        String moduleTitle = groupName.toUpperCase().replace("_", " ");
        if (groupName.equals("internal")) {
            groupName = "~" + groupName;
        }
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
