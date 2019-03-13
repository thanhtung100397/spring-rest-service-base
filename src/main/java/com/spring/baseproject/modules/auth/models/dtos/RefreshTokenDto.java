package com.spring.baseproject.modules.auth.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class RefreshTokenDto {
    @ApiModelProperty(notes = "refresh token", example = "NOT_EMPTY", required = true)
    @NotEmpty
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
