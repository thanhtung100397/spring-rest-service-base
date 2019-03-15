package com.spring.baseproject.components.rbac;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.annotations.rbac.RoleBaseAccessControl;
import com.spring.baseproject.utils.auth.RouteScannerUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Primary
public class RBACAuthApiFilter implements RouteScannerUtils.ApiFilter {
    @Override
    public boolean allowApi(Class<?> containClass, Method method) {
        return containClass.getDeclaredAnnotation(AuthorizationRequired.class) != null ||
                containClass.getDeclaredAnnotation(RoleBaseAccessControl.class) != null ||
                method.getDeclaredAnnotation(AuthorizationRequired.class) != null ||
                method.getDeclaredAnnotation(RoleBaseAccessControl.class) != null;
    }
}
