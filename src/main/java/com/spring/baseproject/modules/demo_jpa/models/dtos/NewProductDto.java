package com.spring.baseproject.modules.demo_jpa.models.dtos;

import com.spring.baseproject.modules.demo_jpa.models.entities.ProductSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel
public class NewProductDto {
    @ApiModelProperty(notes = "tên của sản phẩm", example = "NOT_EMPTY", position = 1)
    @NotEmpty
    private String name;
    @ApiModelProperty(notes = "các tag của sản phẩm", position = 3)
    private List<String> tags;
    @ApiModelProperty(notes = "id của loại sản phẩm", position = 4)
    private int productTypeID;
    @ApiModelProperty(notes = "loại size sản phẩm", example = "NOT_EMPTY, VALUE_IN(SM, M, L, XL, XXL)", position = 5)
    private ProductSize productSize;
    @ApiModelProperty(notes = "mô tả về sản phẩm", example = "NOT_EMPTY", position = 6)
    @NotEmpty
    private String description;

    // always create an empty constructor
    public NewProductDto() {
    }

    // always create full getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(int productTypeID) {
        this.productTypeID = productTypeID;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
