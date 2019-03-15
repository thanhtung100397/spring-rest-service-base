package com.spring.baseproject.modules.rbac.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartedUserDto {
    @JsonProperty("password")
    private String password;
    @JsonProperty("role_name")
    private String roleName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
