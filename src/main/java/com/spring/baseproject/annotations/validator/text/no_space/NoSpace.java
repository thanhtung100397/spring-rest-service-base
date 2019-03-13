package com.spring.baseproject.annotations.validator.text.no_space;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSpaceValidator.class)
public @interface NoSpace {
    String message() default "string must not contain space character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
