package com.spring.baseproject.exceptions.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.baseproject.constants.ResponseValue;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CustomOAuth2ExceptionSerializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {
    private ResponseValue responseValue;

    public CustomOAuth2Exception(OAuth2Exception e) {
        super(e.getMessage());
        switch (e.getMessage()){
            case "Bad credentials": {
                responseValue = ResponseValue.WRONG_USERNAME_OR_PASSWORD;
            }
            break;

            case "Cannot convert access token to JSON": {
                responseValue = ResponseValue.INVALID_TOKEN;
            }
            break;

            default:{
                responseValue = ResponseValue.EXPIRED_TOKEN;
                break;
            }
        }
    }

    public ResponseValue getResponseValue() {
        return responseValue;
    }
}
