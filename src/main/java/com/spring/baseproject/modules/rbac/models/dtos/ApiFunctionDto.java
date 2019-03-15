package com.spring.baseproject.modules.rbac.models.dtos;

import com.spring.baseproject.modules.rbac.models.entities.ApiFunction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class ApiFunctionDto {
    @ApiModelProperty(notes = "tên của function")
    private String name;
    @ApiModelProperty(notes = "mô tả về function", position = 1)
    private String description;
    @ApiModelProperty(notes = "các api của function", position = 2)
    private List<ApiDto> apis = new ArrayList<>();

    public ApiFunctionDto(ApiFunction apiFunction) {
        this.name = apiFunction.getName();
        this.description = apiFunction.getDescription();
    }

    public ApiFunctionDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ApiDto> getApis() {
        return apis;
    }

    public void setApis(List<ApiDto> apis) {
        this.apis = apis;
    }

    public void addApi(ApiDto apiDto) {
        this.apis.add(apiDto);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ApiFunctionDto &&
                ((ApiFunctionDto) obj).name.equals(this.name);
    }
}
