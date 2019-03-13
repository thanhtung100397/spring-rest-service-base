package com.spring.baseproject.exceptions.auth;

import com.spring.baseproject.constants.ResponseValue;

public class RoleIsNotAllowedException extends Exception {
    public RoleIsNotAllowedException() {
        super(ResponseValue.ROLE_NOT_ALLOWED.message());
    }
}
