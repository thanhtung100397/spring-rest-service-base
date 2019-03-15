package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.auth.AuthorizationException;
import com.spring.baseproject.modules.auth.models.dtos.AuthorizedUser;
import com.spring.baseproject.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthorizationService implements IAuthorization {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void authorizeUser(String method, String route,
                              AuthorizedUser authorizedUser, Collection<? extends GrantedAuthority> authorities)
            throws AuthorizationException {
        boolean authorizationResult = userRepository.isUserBanned(authorizedUser.getUserID());
        if (authorizationResult) {
            throw new AuthorizationException(ResponseValue.USER_BANNED);
        }
    }
}
