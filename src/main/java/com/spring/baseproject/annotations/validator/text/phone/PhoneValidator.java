package com.spring.baseproject.annotations.validator.text.phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private String phoneRegex;

    @Override
    public void initialize(Phone constraintAnnotation) {
        phoneRegex = constraintAnnotation.phoneRegex();
    }

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
        return inputValue == null || inputValue.matches(phoneRegex);
    }
}
