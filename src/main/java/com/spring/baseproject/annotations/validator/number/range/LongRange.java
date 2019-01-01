package com.spring.baseproject.annotations.validator.number.range;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LongRangeValidator.class)
public @interface LongRange {
    String message() default "{com.worksvn.cssservice.validation.range.LongRangeValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String start();
    String end();
}
