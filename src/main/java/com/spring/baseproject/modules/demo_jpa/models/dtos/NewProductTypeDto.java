package com.spring.baseproject.modules.demo_jpa.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class NewProductTypeDto {
    @ApiModelProperty(notes = "tên của loại sản phẩm", example = "NOT_EMPTY", position = 1)
    @NotEmpty
    private String name;

    // always create an empty constructor
    public NewProductTypeDto() {
    }

    // always create full getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
