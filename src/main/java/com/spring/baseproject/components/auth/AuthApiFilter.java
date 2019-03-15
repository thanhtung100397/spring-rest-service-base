package com.spring.baseproject.components.auth;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.utils.auth.RouteScannerUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class AuthApiFilter implements RouteScannerUtils.ApiFilter {
    @Override
    public boolean allowApi(Class<?> containClass, Method method) {
        return containClass.getDeclaredAnnotation(AuthorizationRequired.class) != null ||
                method.getDeclaredAnnotation(AuthorizationRequired.class) != null;
    }
}
