package com.spring.baseproject.configs.auth;

import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.utils.base.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
public class ResourceServerBeans {
    @Bean
    public NoAuthorizationRequiredRoutes permitRoutesDictionary() {
        return new NoAuthorizationRequiredRoutes();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            String responseBodyJson = JacksonObjectMapper.getInstance()
                                                         .writeValueAsString(new BaseResponseBody<>(ResponseValue.AUTHENTICATION_REQUIRED, null));
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(ResponseValue.AUTHENTICATION_REQUIRED.httpStatus().value());
            response.getWriter().write(responseBodyJson);
        };
    }
}
