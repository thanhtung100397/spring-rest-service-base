package com.spring.baseproject.modules.auth.models.entities;

import com.spring.baseproject.modules.auth.models.dtos.NewRoleDto;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity
@Table(name = "role")
@ApiModel
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RoleType type;

    public Role() {
    }

    public Role(Integer id, String name, RoleType type) {
        this.id = id == null? 0 : id;
        this.name = name;
        this.type = type;
    }

    public Role(String name) {
        setName(name);
    }

    public Role(String name, RoleType roleType) {
        setName(name);
        setType(roleType);
    }

    public void update(NewRoleDto newRoleDto) {
        setName(newRoleDto.getName());
        setType(newRoleDto.getRoleType());
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

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Role && ((Role) obj).id == id;
    }
}
