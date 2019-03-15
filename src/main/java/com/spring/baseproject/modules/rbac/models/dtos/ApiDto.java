package com.spring.baseproject.modules.rbac.models.dtos;

import com.spring.baseproject.modules.rbac.models.entities.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ApiDto {
    @ApiModelProperty(notes = "id của api")
    private int id;
    @ApiModelProperty(notes = "http method của api", position = 1)
    private String method;
    @ApiModelProperty(notes = "path của api", position = 2)
    private String route;
    @ApiModelProperty(notes = "mô tả về api", position = 3)
    private String description;

    public ApiDto() {
    }

    public ApiDto(Api api) {
        setId(api.getId());
        setMethod(api.getMethod() != null? api.getMethod().getName() : null);
        setRoute(api.getRoute());
        setDescription(api.getDescription());
    }

    public ApiDto(int id,
                  String method,
                  String route,
                  String description) {
        this.id = id;
        this.method = method;
        this.route = route;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ApiDto &&
                ((ApiDto) obj).id == this.id;
    }
}
