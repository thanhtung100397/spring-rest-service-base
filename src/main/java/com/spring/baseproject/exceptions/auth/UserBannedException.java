package com.spring.baseproject.exceptions.auth;

import com.spring.baseproject.constants.ResponseValue;

public class UserBannedException extends Exception {
    public UserBannedException(String reason) {
        super(ResponseValue.USER_BANNED.message());
    }
}
