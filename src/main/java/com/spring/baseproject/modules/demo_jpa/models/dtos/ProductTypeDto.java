package com.spring.baseproject.modules.demo_jpa.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ProductTypeDto {
    @ApiModelProperty(notes = "id của loại sản phầm")
    private int id;
    @ApiModelProperty(notes = "tên của loại sản phầm", position = 1)
    private String name;

    // always create an empty constructor
    public ProductTypeDto() {
    }

    public ProductTypeDto(Integer id, String name) {// id should be Integer, not int, because in some special case, it may be null
        this.id = id == null? -1 : id;
        this.name = name;
    }

    // always create full getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
