package com.spring.baseproject.modules.auth.models.dtos;

import com.spring.baseproject.annotations.validator.text.length.MinLength;
import com.spring.baseproject.annotations.validator.text.no_space.NoSpace;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class NewUserDto {
    @ApiModelProperty(notes = "username của người dùng", example = "NOT_EMPTY, NO_SPACE")
    @NoSpace
    private String username;
    @ApiModelProperty(notes = "password của người dùng", example = "NOT_EMPTY, MIN_LENGTH=6", position = 1)
    @NotEmpty
    @MinLength(6)
    private String password;
    @ApiModelProperty(notes = "id của quyền", example = "NULLABLE", position = 2)
    private Integer roleID;

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

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }
}
