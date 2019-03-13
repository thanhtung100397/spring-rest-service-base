package com.spring.baseproject.configs.auth;

import com.spring.baseproject.modules.auth.models.dtos.AuthorizedUser;
import com.spring.baseproject.modules.auth.models.dtos.CustomUserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.*;

@Configuration
public class AccessTokenConfig {
    @Value("${application.oauth2.authorization-server.access-token.validity-seconds}")
    private int accessTokenValiditySeconds;
    @Value("${application.oauth2.authorization-server.refresh-token.validity-seconds}")
    private int refreshTokenValiditySeconds;
    @Value("${application.oauth2.authorization-server.token-signing-key}")
    private String jwtSigningKey;

    @Bean
    TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                OAuth2Authentication authentication = super.extractAuthentication(map);
                Authentication userAuthentication = authentication.getUserAuthentication();
                if (userAuthentication != null) {
                    AuthorizedUser authorizedUser = new AuthorizedUser(map);
                    Collection<? extends GrantedAuthority> authorities = userAuthentication.getAuthorities();
                    userAuthentication = new UsernamePasswordAuthenticationToken(authorizedUser,
                            userAuthentication.getCredentials(), authorities);
                }
                return new OAuth2Authentication(authentication.getOAuth2Request(), userAuthentication);
            }
        };
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    DefaultTokenServices tokenServices(TokenStore tokenStore,
                                       TokenEnhancer customTokenEnhancer,
                                       JwtAccessTokenConverter jwtAccessTokenConverter) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, jwtAccessTokenConverter));

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        return defaultTokenServices;
    }

    @Bean
    TokenEnhancer customTokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> info = new HashMap<>();
            // Custom claims for generated access token here
            try {
                CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getUserAuthentication().getPrincipal();
                info.put("user_id", customUserDetail.getUserID());
            } catch (Exception ignore) {
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            return accessToken;
        };
    }
}
