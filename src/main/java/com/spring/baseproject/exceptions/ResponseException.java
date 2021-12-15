package com.spring.baseproject.exceptions;

import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import org.springframework.http.HttpStatus;

public class ResponseException extends Exception {
    private HttpStatus httpStatus;
    private BaseResponseBody<?> body;
    private ResponseValue responseValue;

    public ResponseException() {

    }

    public <T> ResponseException(ResponseValue responseValue) {
        this(responseValue, null);
    }

    public <T> ResponseException(ResponseValue responseValue, T body) {
        this(responseValue.httpStatus(), new BaseResponseBody<>(responseValue, body));
        this.responseValue = responseValue;
    }

    public ResponseException(HttpStatus httpStatus, BaseResponseBody<?> body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getSpecialCode() {
        if (body != null) {
            return body.getCode();
        }
        return -1;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BaseResponseBody<?> getBody() {
        return body;
    }

    public void setBody(BaseResponseBody<?> body) {
        this.body = body;
    }

    public ResponseValue getResponseValue() {
        return responseValue;
    }


}
