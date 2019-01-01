package com.spring.baseproject.annotations.validator.text.length;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinLengthValidator.class)
public @interface MinLength {
    String message() default "string length must be at least {value} character(s)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int value() default 0;
}
