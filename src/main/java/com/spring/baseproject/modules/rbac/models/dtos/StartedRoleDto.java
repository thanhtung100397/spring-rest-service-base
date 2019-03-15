package com.spring.baseproject.modules.rbac.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartedRoleDto {
    @JsonProperty("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
