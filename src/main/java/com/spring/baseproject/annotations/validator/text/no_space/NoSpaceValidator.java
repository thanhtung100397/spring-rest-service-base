package com.spring.baseproject.annotations.validator.text.no_space;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {

    @Override
    public void initialize(NoSpace constraintAnnotation) {

    }

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
        return inputValue == null || !inputValue.contains(" ");
    }
}
