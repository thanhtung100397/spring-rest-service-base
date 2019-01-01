package com.spring.baseproject.modules.demo.models.dtos;

import com.spring.baseproject.annotations.validator.text.no_space.NoSpace;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class UserDto extends UpdateUserDto {
    @ApiModelProperty(notes = "username", example = "NO_SPACE_STRING")
    @NoSpace
    @NotEmpty
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
