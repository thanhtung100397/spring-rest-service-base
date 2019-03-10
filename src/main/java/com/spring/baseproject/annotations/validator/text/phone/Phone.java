package com.spring.baseproject.annotations.validator.text.phone;

import com.spring.baseproject.constants.RegexPartern;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "invalid phone number, require matching regex {phoneRegex}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String phoneRegex() default RegexPartern.PHONE_REGEX;
}
