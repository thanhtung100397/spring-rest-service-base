package com.spring.baseproject.modules.rbac.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModuleDescriptionDto {
    @JsonProperty("description")
    private String description;
    @JsonProperty("priority")
    private Integer priority;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
