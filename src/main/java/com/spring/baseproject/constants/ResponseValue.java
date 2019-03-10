package com.spring.baseproject.constants;

import org.springframework.http.HttpStatus;

public enum ResponseValue {
    //200x OK
    SUCCESS(HttpStatus.OK, "success"),

    //400x Bad request
    MISSING_REQUEST_PARAMS(HttpStatus.BAD_REQUEST, 4001, "missing request param"),
    INVALID_OR_MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, 4002, "invalid or missing request body"),
    FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 4003, "field validation error"),

    //401x Unauthorized

    //404x Not found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4041, "user not found"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, 4042, "product not found"),
    PRODUCT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, 4043, "product type not found"),

    //409x Conflict
    USERNAME_EXISTS(HttpStatus.CONFLICT, 4091, "username exists"),

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
