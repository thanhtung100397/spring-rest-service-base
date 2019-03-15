package com.spring.baseproject.modules.auth.models.entities;

import com.spring.baseproject.modules.auth.models.dtos.NewRoleDto;
import com.spring.baseproject.modules.rbac.models.entities.Api;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@ApiModel
public class Role {
    public static final String NAME = "name";
    public static final String TYPE = "type";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RoleType type;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = Api.ROLES)
    private Set<Api> apis = new HashSet<>();

    public Role() {
    }

    public Role(Integer id, String name, RoleType type) {
        this.id = id == null ? 0 : id;
        this.name = name;
        this.type = type;
    }

    public Role(String name, RoleType type) {
        this.name = name;
        this.type = type;
    }

    public Role(NewRoleDto newRoleDto) {
        update(newRoleDto);
    }

    public void update(NewRoleDto newRoleDto) {
        setName(newRoleDto.getName());
        setType(newRoleDto.getType());
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

    public Set<Api> getApis() {
        return apis;
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
