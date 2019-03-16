package com.spring.baseproject.annotations.validator.text.no_space;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {
    private boolean allowNull;

    @Override
    public void initialize(NoSpace constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
        if (allowNull) {
            return inputValue == null || (!inputValue.equals("") && !inputValue.contains(" "));
        }
        return inputValue != null && !inputValue.equals("") && !inputValue.contains(" ");
    }
}
