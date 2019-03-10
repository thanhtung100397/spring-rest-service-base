package com.spring.baseproject.modules.demo_jpa.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel
public class ProductPreviewDto {
    @ApiModelProperty(notes = "id của sản phẩm")
    private String id;
    @ApiModelProperty(notes = "tên của sản phẩm", position = 1)
    private String name;
    @ApiModelProperty(notes = "ngày tạo sản phẩm", position = 2)
    private Date createdDate;
    @ApiModelProperty(notes = "các tag của sản phẩm", position = 3)
    private List<String> tags;
    @ApiModelProperty(notes = "loại sản phẩm", position = 4)
    private ProductTypeDto productType;

    // always create an empty constructor
    public ProductPreviewDto() {
    }

    public ProductPreviewDto(String id, String name,
                             Date createdDate, List<String> tags,
                             int productTypeID, String productTypeName) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.tags = tags;
        this.productType = new ProductTypeDto(productTypeID, productTypeName);
    }

    // always create full getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ProductTypeDto getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeDto productType) {
        this.productType = productType;
    }
}
