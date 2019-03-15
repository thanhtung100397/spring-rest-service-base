package com.spring.baseproject.modules.rbac.models.dtos;

import com.spring.baseproject.modules.auth.models.entities.RoleType;

public class RBACAuthorizationResult {
    private RoleType roleType;
    private boolean apiAccessible;
    private boolean isBanned;

    public RBACAuthorizationResult(RoleType roleType, Integer apiID, boolean isBanned) {
        this.roleType = roleType;
        this.isBanned = isBanned;
        this.apiAccessible = apiID != null;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public boolean apiAccessible() {
        return apiAccessible;
    }

    public void setApiAccessible(boolean apiAccessible) {
        this.apiAccessible = apiAccessible;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
