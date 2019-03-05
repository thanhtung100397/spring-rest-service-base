package com.spring.baseproject.constants;

import org.springframework.http.HttpStatus;

public enum ResponseValue {
    //200x OK
    SUCCESS(HttpStatus.OK, "success"),

    //400x Bad request
    REQUEST_PARAMS_MISSING(HttpStatus.BAD_REQUEST, 4001, "request param missing"),
    FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 4002, "field validation error"),

    //404x Not found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "item not found"),

    //409x Conflict
    USERNAME_EXISTS(HttpStatus.CONFLICT, "username exists"),

    //500x Internal server error
    UNEXPECTED_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected error occurred");

    private HttpStatus httpStatus;
    private String message;
    private int specialCode;

    ResponseValue(HttpStatus httpStatus, int specialCode, String message) {
        this.httpStatus = httpStatus;
        this.specialCode = specialCode;
        this.message = message;
    }

    ResponseValue(HttpStatus httpStatus, String message) {
        this(httpStatus, httpStatus.value(), message);
    }

    ResponseValue(HttpStatus httpStatus) {
        this(httpStatus, httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public int specialCode() {
        return specialCode;
    }

    public String message() {
        return message;
    }
}
