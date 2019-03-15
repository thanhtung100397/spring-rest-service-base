package com.spring.baseproject.exceptions.auth;

import com.spring.baseproject.constants.ResponseValue;

public class AuthorizationException extends Exception {
    private ResponseValue responseValue;

    public AuthorizationException(ResponseValue responseValue) {
        super(responseValue.message());
        this.responseValue = responseValue;
    }

    public ResponseValue getResponseValue() {
        return responseValue;
    }
}
