package com.spring.baseproject;

import com.fasterxml.classmate.TypeResolver;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseMessageReader;
import com.spring.baseproject.components.swagger.CustomSwaggerResponseModelProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider;
import springfox.documentation.swagger.readers.operation.SwaggerResponseMessageReader;

@SpringBootApplication
public class BaseProjectApplication {

    @Bean
    public SwaggerResponseMessageReader swaggerResponseMessageReader(TypeNameExtractor typeNameExtractor,
                                                                     TypeResolver typeResolver) {
        return new CustomSwaggerResponseMessageReader(typeNameExtractor, typeResolver);
    }

    @Bean
    public SwaggerOperationModelsProvider swaggerOperationModelsProvider(TypeResolver typeResolver) {
        return new CustomSwaggerResponseModelProvider(typeResolver);
    }

    public static void main(String[] args) {
        SpringApplication.run(BaseProjectApplication.class, args);
    }

}

