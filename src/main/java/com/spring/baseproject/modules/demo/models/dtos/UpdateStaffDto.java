package com.spring.baseproject.modules.demo.models.dtos;

import com.spring.baseproject.annotations.validator.text.phone.Phone;
import com.spring.baseproject.constants.RegexPartern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@ApiModel
public class UpdateStaffDto {
    @ApiModelProperty(notes = "tên", example = "NOT_EMPTY, ANY_CHARACTER", position = 1)
    @NotEmpty
    private String firstName;
    @ApiModelProperty(notes = "họ", example = "NOT_EMPTY, ANY_CHARACTER", position = 2)
    @NotEmpty
    private String lastName;
    @ApiModelProperty(notes = "email", example = "EMAIL", position = 3)
    @Email
    private String email;
    @ApiModelProperty(notes = "số di động", example = "REGEX " + RegexPartern.PHONE_REGEX, position = 4)
    @Phone
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
