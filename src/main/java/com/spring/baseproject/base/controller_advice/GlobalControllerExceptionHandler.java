package com.spring.baseproject.base.controller_advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.FieldValidationError;
import com.spring.baseproject.base.models.MessageDto;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.utils.base.ClassUtils;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @ExceptionHandler(ResponseException.class)
    @ResponseBody
    public BaseResponse<?> onResponseError(ResponseException e) {
        return new BaseResponse<>(e.getHttpStatus(), e.getBody());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public BaseResponse<?> onAccessDenied(Exception e) {
        return new BaseResponse<>(ResponseValue.ACCESS_DENIED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<?> onInternalServerError(Exception e) {
        e.printStackTrace();
        if (activeProfile.equalsIgnoreCase("dev")) {
            return new BaseResponse<>(ResponseValue.UNEXPECTED_ERROR_OCCURRED, new MessageDto(e.toString()));
        } else {
            return new BaseResponse<>(ResponseValue.UNEXPECTED_ERROR_OCCURRED);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> onDtoValidationError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        return handleFieldErrors(errors);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> onBindValueDtoError(BindException e) {
        BindingResult result = e.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        return handleFieldErrors(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> onRequestParamMissingError(MissingServletRequestParameterException e) {
        return new BaseResponse<>(ResponseValue.INVALID_OR_MISSING_REQUEST_PARAMS);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> onRequestBodyError(HttpMessageNotReadableException e) {
        return new BaseResponse<>(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY);
    }

    private BaseResponse<?> handleFieldErrors(List<ObjectError> fieldErrors) {
        List<FieldValidationError> invalidFieldDtos = new ArrayList<>();
        for (ObjectError objectError : fieldErrors) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                String fieldName = getErrorFieldName(fieldError);
                String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
                invalidFieldDtos.add(new FieldValidationError(fieldName, localizedErrorMessage));
            } else {
                invalidFieldDtos.add(new FieldValidationError(objectError.getObjectName(), objectError.getDefaultMessage()));
            }
        }
        return new BaseResponse<>(ResponseValue.INVALID_FIELDS, invalidFieldDtos);
    }

    private String getErrorFieldName(FieldError fieldError) {
        String fieldName = fieldError.getField();
        ConstraintViolationImpl constraintViolation = fieldError.unwrap(ConstraintViolationImpl.class);
        try {
            String[] fieldPaths = fieldName.split("\\.");
            Field invalidField = ClassUtils.getNestedClassField(constraintViolation.getRootBeanClass(), fieldPaths);
            String alternativeFieldName = getAlternativeFieldName(invalidField);
            if (alternativeFieldName != null) {
                fieldPaths[fieldPaths.length - 1] = alternativeFieldName;
                return String.join(".", fieldPaths);
            }
        } catch (NoSuchFieldException ignored) {

        }
        return fieldName;
    }

    private String getAlternativeFieldName(Field field) {
        com.fasterxml.jackson.annotation.JsonProperty jsonProperty1 = field.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class);
        if (jsonProperty1 != null) {
            return jsonProperty1.value();
        }
        org.codehaus.jackson.annotate.JsonProperty jsonProperty2 = field.getAnnotation(org.codehaus.jackson.annotate.JsonProperty.class);
        if (jsonProperty2 != null) {
            return jsonProperty2.value();
        }
        return null;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        return fieldError.getDefaultMessage();
    }
}
