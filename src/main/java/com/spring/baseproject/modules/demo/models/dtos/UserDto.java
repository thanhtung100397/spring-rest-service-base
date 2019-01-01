package com.spring.baseproject.modules.demo.models.dtos;

import com.spring.baseproject.annotations.validator.text.length.MaxLength;
import com.spring.baseproject.annotations.validator.text.length.MinLength;
import com.spring.baseproject.annotations.validator.text.no_space.NoSpace;
import com.spring.baseproject.annotations.validator.text.phone.phone.Phone;
import com.spring.baseproject.constants.RegexPartern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@ApiModel
public class UserDto {
    @ApiModelProperty(notes = "username", example = "NO_SPACE_STRING")
    @NoSpace
    @NotEmpty
    private String username;
    @ApiModelProperty(notes = "password", example = "ANY_CHARACTER, MIN_LENGTH=6, MAX_LENGTH=35", position = 1)
    @MinLength(6)
    @MaxLength(35)
    @NotEmpty
    private String password;
    @ApiModelProperty(notes = "email", example = "EMAIL", position = 2)
    @Email
    private String email;
    @ApiModelProperty(notes = "phone", example = "REGEX " + RegexPartern.PHONE_REGEX, position = 3)
    @Phone
    private String phone;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
