package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.auth.models.dtos.AuthenticationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class OAuth2AuthService {
    @Value("${application.oauth2.authentication.grant_type.allow}")
    private Set<String> allowGrantTypes;
    @Value("${application.oauth2.authorization-server.access-token.validity-seconds}")
    private long accessTokenExpirationTime;
    @Value("${application.oauth2.authorization-server.refresh-token.validity-seconds}")
    private long refreshTokenExpirationTime;

    @Autowired
    private AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();

    public AuthenticationResult authenticateByUsernamePassword(String clientID, String secret,
                                                               String username, String password) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("username", username.toLowerCase().trim());
        params.put("password", password);
        return authenticate(clientID, secret, params);
    }

    public AuthenticationResult authenticateByRefreshToken(String clientID, String secret,
                                                           String refreshToken) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        return authenticate(clientID, secret, params);
    }

    private AuthenticationResult authenticate(String clientID, String secret,
                                              Map<String, String> params) throws Exception {
        if (clientID == null || clientID.isEmpty() || secret == null || secret.isEmpty()) {
            throw new ResponseException(ResponseValue.MISSING_CLIENT_ID_OR_SECRET);
        }

        ClientDetails authenticatedClient;
        try {
            authenticatedClient = getClientDetailService().loadClientByClientId(clientID);
        } catch (ClientRegistrationException e) {
            throw new ResponseException(ResponseValue.WRONG_CLIENT_ID_OR_SECRET);
        }
        if (!passwordEncoder.matches(secret, authenticatedClient.getClientSecret())) {
            throw new ResponseException(ResponseValue.WRONG_CLIENT_ID_OR_SECRET);
        }

        TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(params, authenticatedClient);

        try {
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
        } catch (InvalidScopeException e) {
            throw new ResponseException(ResponseValue.ACCESS_DENIED);
        }
        if (!StringUtils.hasText(tokenRequest.getGrantType())) {
            throw new ResponseException(ResponseValue.MISSING_GRANT_TYPE);
        }
        if (!allowGrantTypes.contains(tokenRequest.getGrantType())) {
            throw new ResponseException(ResponseValue.GRANT_TYPE_NOT_SUPPORTED);
        }
        if (isAuthCodeRequest(params)) {
            if (!tokenRequest.getScope().isEmpty()) {
                tokenRequest.setScope(Collections.emptySet());
            }
        }
        if (isRefreshTokenRequest(params)) {
            tokenRequest.setScope(OAuth2Utils.parseParameterList(params.get(OAuth2Utils.SCOPE)));
        }
        try {
            OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
            return new AuthenticationResult(token.getTokenType(),
                    token.getValue(), token.getRefreshToken().getValue(),
                    token.getAdditionalInformation(),
                    accessTokenExpirationTime, refreshTokenExpirationTime
            );
        } catch (InvalidGrantException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals("Bad credentials")) {
                throw new ResponseException(ResponseValue.WRONG_USERNAME_OR_PASSWORD);
            } else if (errorMessage.startsWith("Invalid refresh token")) {
                throw new ResponseException(ResponseValue.INVALID_TOKEN);
            } else if (errorMessage.startsWith("Wrong client for this refresh token")) {
                throw new ResponseException(ResponseValue.WRONG_CLIENT_ID_OR_SECRET);
            }
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            if (errorMessage.startsWith("Invalid refresh token (expired)")) {
                throw new ResponseException(ResponseValue.EXPIRED_TOKEN);
            } else if (errorMessage.equals("Cannot convert access token to JSON")) {
                throw new ResponseException(ResponseValue.INVALID_TOKEN);
            } else if (errorMessage.equals("Encoded token is not a refresh token")) {
                throw new ResponseException(ResponseValue.INVALID_TOKEN);
            }
        }
        throw new ResponseException(ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

    private ClientDetailsService getClientDetailService() {
        return authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getClientDetailsService();
    }

    private OAuth2RequestFactory getOAuth2RequestFactory() {
        return authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getOAuth2RequestFactory();
    }

    private TokenGranter getTokenGranter() {
        return authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getTokenGranter();
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }
}
