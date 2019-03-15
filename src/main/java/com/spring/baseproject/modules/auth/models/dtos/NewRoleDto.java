package com.spring.baseproject.modules.auth.models.dtos;

import com.spring.baseproject.modules.auth.models.entities.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class NewRoleDto {
    @ApiModelProperty(notes = "tên của quyền", required = true)
    @NotEmpty
    private String name;
    @ApiModelProperty(notes = "loại quyền", example = "NULLABLE, VALUE_IN(ROOT, ADMIN)")
    private RoleType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}
