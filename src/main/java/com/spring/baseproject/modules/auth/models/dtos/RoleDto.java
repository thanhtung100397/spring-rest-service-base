package com.spring.baseproject.modules.auth.models.dtos;

import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class RoleDto {
    @ApiModelProperty(notes = "id của quyền")
    private int id;
    @ApiModelProperty(notes = "tên của quyền", position = 1)
    private String name;
    @ApiModelProperty(notes = "loại quyền", position = 2)
    private RoleType type;

    // always create an empty constructor
    public RoleDto() {
    }

    public RoleDto(Integer id, // should be Integer, not int
                   String name, RoleType type) {
        this.id = id == null? -1 : id;
        this.name = name;
        this.type = type;
    }

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.type = role.getType();
    }


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

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}
