package com.spring.baseproject.exceptions;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse handleInternalServerError(Exception e) {
        e.printStackTrace();
        return new BaseResponse(ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }
}
