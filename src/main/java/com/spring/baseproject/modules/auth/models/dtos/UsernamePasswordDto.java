package com.spring.baseproject.modules.auth.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class UsernamePasswordDto {
    @ApiModelProperty(notes = "tên đăng nhập", example = "NOT_EMPTY", required = true)
    @NotEmpty
    private String username;
    @ApiModelProperty(notes = "mật khẩu", example = "NOT_EMPTY", required = true, position = 1)
    @NotEmpty
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
