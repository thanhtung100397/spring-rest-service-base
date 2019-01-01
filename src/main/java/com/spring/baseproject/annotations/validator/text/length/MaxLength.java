package com.spring.baseproject.annotations.validator.text.length;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxLengthValidator.class)
public @interface MaxLength {
    String message() default "string length must be at most {value} character(s)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int value() default 0;
}
