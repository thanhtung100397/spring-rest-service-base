package com.spring.baseproject.modules.demo_auth.models.dtos;

import com.spring.baseproject.modules.auth.models.dtos.RoleDto;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class UserDto {
    @ApiModelProperty(notes = "id của người dùng")
    private String id;
    @ApiModelProperty(notes = "tên đăng nhập của người dùng", position = 1)
    private String username;
    @ApiModelProperty(notes = "password đã được hash của người dùng", position = 2)
    private String password;
    @ApiModelProperty(notes = "tài khoản có đang bị vô hiệu hóa hay không", position = 3)
    private boolean isBanned;
    @ApiModelProperty(notes = "thời điểm thực hiện xác thực cuối cùng", position = 4)
    private Date lastActive;
    @ApiModelProperty(notes = "quyền hiện tại của người dùng", example = "NULLABLE", position = 5)
    private RoleDto role;

    // always create an empty constructor
    public UserDto() {
    }

    public UserDto(String id, String username, String password,
                   Boolean isBanned, //should be Boolean, not boolean, because isBanned may be null
                   Date lastActive, Integer roleID, String roleName, RoleType roleType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isBanned = isBanned == null ? false : isBanned;
        this.lastActive = lastActive;
        if (roleID != null) {
            this.role = new RoleDto(roleID, roleName, roleType);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }
}
