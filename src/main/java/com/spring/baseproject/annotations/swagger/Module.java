package com.spring.baseproject.annotations.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String title();
    String description() default "No description";
    String version() default "1.0";
    String developBy() default "";
    String contactEmail() default "";
}
