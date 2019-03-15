package com.spring.baseproject.modules.rbac.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StartedRolesUsersDto {
    @JsonProperty("started_roles")
    private Map<String, StartedRoleDto> startedRoles;
    @JsonProperty("started_users")
    private Map<String, StartedUserDto> startedUsers;

    public Map<String, StartedRoleDto> getStartedRoles() {
        return startedRoles;
    }

    public void setStartedRoles(Map<String, StartedRoleDto> startedRoles) {
        this.startedRoles = startedRoles;
    }

    public Map<String, StartedUserDto> getStartedUsers() {
        return startedUsers;
    }

    public void setStartedUsers(Map<String, StartedUserDto> startedUsers) {
        this.startedUsers = startedUsers;
    }
}
