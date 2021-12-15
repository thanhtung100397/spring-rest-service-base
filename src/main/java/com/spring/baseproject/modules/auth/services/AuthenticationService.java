package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.modules.auth.models.dtos.AuthenticationResult;
import com.spring.baseproject.modules.auth.models.dtos.RefreshTokenDto;
import com.spring.baseproject.modules.auth.models.dtos.UsernamePasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private OAuth2AuthService oAuth2AuthService;

    public AuthenticationResult authenticateByUsernameAndPassword(String clientID, String secret,
                                                                  UsernamePasswordDto usernamePassword) throws Exception {
        return oAuth2AuthService.authenticateByUsernamePassword(clientID, secret,
                usernamePassword.getUsername(), usernamePassword.getPassword());
    }

    public AuthenticationResult authenticateByRefreshToken(String clientID, String secret,
                                                           RefreshTokenDto refreshToken) throws Exception {
        return oAuth2AuthService.authenticateByRefreshToken(clientID, secret, refreshToken.getRefreshToken());
    }
}
