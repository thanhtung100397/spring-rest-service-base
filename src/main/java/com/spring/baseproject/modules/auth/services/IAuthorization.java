package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.exceptions.auth.AuthorizationException;
import com.spring.baseproject.modules.auth.models.dtos.AuthorizedUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface IAuthorization {
    void authorizeUser(String method, String route, AuthorizedUser authorizedUser,
                       Collection<? extends GrantedAuthority> authorities) throws AuthorizationException;
}
