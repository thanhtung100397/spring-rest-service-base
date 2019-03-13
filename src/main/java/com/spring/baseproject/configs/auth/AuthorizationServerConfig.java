package com.spring.baseproject.configs.auth;

import com.spring.baseproject.exceptions.auth.CustomOAuth2Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.*;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${application.oauth2.authorization-server.trusted-client.web.id}")
    private String trustedWebClientID;
    @Value("${application.oauth2.authorization-server.trusted-client.web.secret}")
    private String trustedWebClientSecret;
    @Value("${application.oauth2.authorization-server.trusted-client.swagger-ui.id:swagger_ui_client}")
    private String swaggerUIClientID;
    @Value("${application.oauth2.authorization-server.trusted-client.swagger-ui.secret:swagger_ui_secret}")
    private String swaggerUIClientSecret;
    @Value("${application.oauth2.resource-server.id}")
    private String resourceServerID;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private WebResponseExceptionTranslator authExceptionTranslator;

    @Bean
    public WebResponseExceptionTranslator authExceptionTranslator() {
        return e -> {
            if (e instanceof OAuth2Exception) {
                CustomOAuth2Exception customOAuth2Exception = new CustomOAuth2Exception((OAuth2Exception) e);
                return ResponseEntity
                        .status(customOAuth2Exception.getResponseValue().httpStatus())
                        .body(customOAuth2Exception);
            } else {
                throw e;
            }
        };
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenServices(tokenServices)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(authExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(trustedWebClientID)
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .authorities("ROLE_TRUSTED_CLIENT")
                .scopes("read", "write")
                .resourceIds(resourceServerID)
                .secret(passwordEncoder.encode(trustedWebClientSecret))
                .and()
                .withClient(swaggerUIClientID)
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .authorities("ROLE_TRUSTED_CLIENT")
                .scopes("read", "write")
                .resourceIds(resourceServerID)
                .secret(passwordEncoder.encode(swaggerUIClientSecret));
    }
}
