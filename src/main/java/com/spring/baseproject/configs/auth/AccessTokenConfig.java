package com.spring.baseproject.configs.auth;

import com.spring.baseproject.modules.auth.models.dtos.CustomUserDetail;
import com.spring.baseproject.modules.auth.services.QueryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
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

    @Autowired
    private QueryUserDetailsService userDetailService;

    @Bean
    TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter(UserAuthenticationConverter userAuthenticationConverter) {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    public UserAuthenticationConverter userAuthenticationConverter() {
        DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailService);
        return defaultUserAuthenticationConverter;
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
}
