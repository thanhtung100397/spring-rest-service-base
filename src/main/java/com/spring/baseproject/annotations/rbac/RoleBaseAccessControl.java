package com.spring.baseproject.annotations.rbac;

import com.spring.baseproject.modules.auth.models.entities.RoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleBaseAccessControl {
    RoleType[] defaultAccess() default {};
}
