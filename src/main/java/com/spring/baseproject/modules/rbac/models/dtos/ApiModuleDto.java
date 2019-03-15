package com.spring.baseproject.modules.rbac.models.dtos;

import com.spring.baseproject.modules.rbac.models.entities.ApiModule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class ApiModuleDto {
    @ApiModelProperty(notes = "tên module")
    private String name;
    @ApiModelProperty(notes = "mô tả về module", position = 1)
    private String description;
    @ApiModelProperty(notes = "mức dộ ưu tiên (sort) của module", position = 2)
    private int priority;
    @ApiModelProperty(notes = "các function của module", position = 3)
    private List<ApiFunctionDto> apiFunctions = new ArrayList<>();

    public ApiModuleDto(ApiModule apiModule) {
        this.name = apiModule.getName();
        this.description = apiModule.getDescription();

        setName(apiModule.getName());
        setDescription(apiModule.getDescription());
        setPriority(apiModule.getPriority());
    }

    public ApiModuleDto() {

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ApiFunctionDto> getApiFunctions() {
        return apiFunctions;
    }

    public void setApiFunctions(List<ApiFunctionDto> apiFunctions) {
        this.apiFunctions = apiFunctions;
    }

    public void addApiFunction(ApiFunctionDto apiFunctionDto) {
        this.apiFunctions.add(apiFunctionDto);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ApiModuleDto &&
                ((ApiModuleDto) obj).name.equals(this.name);
    }
}
