package com.spring.baseproject.modules.auth.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel
public class AuthenticationResult {
    @ApiModelProperty(notes = "id của user")
    private String userID;
    @ApiModelProperty(notes = "loại của token", position = 1)
    private String tokenType;
    @ApiModelProperty(notes = "id của token", position = 2)
    private String jti;
    @ApiModelProperty(notes = "access token của user", position = 3)
    private String accessToken;
    @ApiModelProperty(notes = "refresh token của user", position = 4)
    private String refreshToken;
    @ApiModelProperty(notes = "thời gian hiệu lực (giây) của access token", position = 5)
    private long accessTokenExpSecs;
    @ApiModelProperty(notes = "thời gian hiệu lực (giây) của refresh token", position = 6)
    private long refreshTokenExpSecs;

    public AuthenticationResult() {
    }

    public AuthenticationResult(String tokenType,
                                String accessToken, String refreshToken,
                                Map<String, ?> additionInfo,
                                long accessTokenExpSecs, long refreshTokenExpSecs) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.accessTokenExpSecs = accessTokenExpSecs;
        this.refreshToken = refreshToken;
        this.refreshTokenExpSecs = refreshTokenExpSecs;
        this.userID = (String) additionInfo.get("user_id");
        this.jti = (String) additionInfo.get("jti");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getAccessTokenExpSecs() {
        return accessTokenExpSecs;
    }

    public void setAccessTokenExpSecs(long accessTokenExpSecs) {
        this.accessTokenExpSecs = accessTokenExpSecs;
    }

    public long getRefreshTokenExpSecs() {
        return refreshTokenExpSecs;
    }

    public void setRefreshTokenExpSecs(long refreshTokenExpSecs) {
        this.refreshTokenExpSecs = refreshTokenExpSecs;
    }
}
