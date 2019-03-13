package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.auth.models.dtos.AuthenticationResult;
import com.spring.baseproject.modules.auth.models.dtos.OriginAuthenticationResult;
import com.spring.baseproject.modules.auth.models.dtos.RefreshTokenDto;
import com.spring.baseproject.modules.auth.models.dtos.UsernamePasswordDto;
import com.spring.baseproject.utils.base.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {
    @Value("${server.port:8080}")
    private int serverPort;
    @Value("${application.oauth2.authorization-server.access-token.validity-seconds:0}")
    private long accessTokenExpirationTime;
    @Value("${application.oauth2.authorization-server.refresh-token.validity-seconds:0}")
    private long refreshTokenExpirationTime;

    @Autowired
    private RestTemplate restTemplate;

    public BaseResponse authenticateByUsernameAndPassword(String invokerBasicAuth,
                                                          UsernamePasswordDto usernamePasswordDto) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("username", usernamePasswordDto.getUsername());
        requestBody.add("password", usernamePasswordDto.getPassword());
        return executeAuthenticationRequest(invokerBasicAuth, requestBody);
    }

    public BaseResponse authenticateByRefreshToken(String invokerBasicAuth,
                                                   RefreshTokenDto refreshTokenDto) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshTokenDto.getRefreshToken());
        return executeAuthenticationRequest(invokerBasicAuth, requestBody);
    }

    private BaseResponse executeAuthenticationRequest(String invokerBasicAuth,
                                                      LinkedMultiValueMap<String, String> requestBody) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("Authorization", invokerBasicAuth);
        try {
            OriginAuthenticationResult originAuthResult = restTemplate
                    .postForObject("http://localhost:" + serverPort + "/oauth/token",
                            new HttpEntity<>(requestBody, requestHeader),
                            OriginAuthenticationResult.class);

            AuthenticationResult result = new AuthenticationResult(originAuthResult,
                    accessTokenExpirationTime, refreshTokenExpirationTime);
            return new BaseResponse<>(ResponseValue.SUCCESS, result);
        } catch (HttpStatusCodeException e) {
            try {
                BaseResponseBody responseBody = JacksonObjectMapper.getInstance()
                        .readValue(e.getResponseBodyAsByteArray(), BaseResponseBody.class);
                if (responseBody.getCode() == ResponseValue.AUTHENTICATION_REQUIRED.specialCode()) {
                    return new BaseResponse(ResponseValue.WRONG_CLIENT_ID_OR_SECRET);
                }
                return new BaseResponse(e.getStatusCode(), responseBody);
            } catch (Exception mappingException) {
                throw e;
            }
        }
    }
}