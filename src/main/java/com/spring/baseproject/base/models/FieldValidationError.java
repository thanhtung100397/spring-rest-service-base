package com.spring.baseproject.base.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FieldValidationError {
    @ApiModelProperty(notes = "Name of error field")
    private String target;
    @ApiModelProperty(notes = "Requirement for error field", position = 1)
    private String required;

    public FieldValidationError() {
    }

    public FieldValidationError(String target, String required) {
        this.target = target;
        this.required = required;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }
}
