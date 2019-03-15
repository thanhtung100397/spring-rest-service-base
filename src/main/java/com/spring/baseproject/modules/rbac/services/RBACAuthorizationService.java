package com.spring.baseproject.modules.rbac.services;

import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.auth.AuthorizationException;
import com.spring.baseproject.modules.auth.models.dtos.AuthorizedUser;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import com.spring.baseproject.modules.auth.services.IAuthorization;
import com.spring.baseproject.modules.rbac.models.dtos.RBACAuthorizationResult;
import com.spring.baseproject.modules.rbac.repositories.RBACUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Primary
public class RBACAuthorizationService implements IAuthorization {
    @Autowired
    private RBACUserRepository rbacUserRepository;

    @Override
    public void authorizeUser(String method, String route,
                              AuthorizedUser authorizedUser, Collection<? extends GrantedAuthority> authorities)
            throws AuthorizationException {
        if (authorities.size() == 0) {
            throw new AuthorizationException(ResponseValue.ROLE_NOT_ALLOWED);
        }
        RBACAuthorizationResult authorizationResult = rbacUserRepository
                .authorizeUser(authorizedUser.getUserID(), method, route);
        if (authorizationResult == null || authorizationResult.isBanned()) {
            throw new AuthorizationException(ResponseValue.USER_BANNED);
        }
        if (authorizationResult.getRoleType() != RoleType.ROOT && !authorizationResult.apiAccessible()) {
            throw new AuthorizationException(ResponseValue.ROLE_NOT_ALLOWED);
        }
    }
}
