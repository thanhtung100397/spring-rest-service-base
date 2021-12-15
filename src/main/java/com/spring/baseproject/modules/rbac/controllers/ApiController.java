package com.spring.baseproject.modules.rbac.controllers;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.annotations.rbac.RoleBaseAccessControl;
import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.rbac.models.dtos.ApiModuleDto;
import com.spring.baseproject.modules.rbac.services.RBACService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth/rbac")
@Api(description = "Quản lý quyền truy cập")
public class ApiController {
    @Autowired
    private RBACService RBACService;

    @ApiOperation(value = "Xem danh sách các api yêu cầu quyền truy cập",
            notes = "Mặc định cho phép truy cập với token của người dùng bất kì",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.INVALID_TOKEN),
            @Response(responseValue = ResponseValue.EXPIRED_TOKEN)
    })
    @AuthorizationRequired
    @GetMapping("/apis")
    public List<ApiModuleDto> getRoutes() {
        return RBACService.getApis();
    }

    @ApiOperation(value = "Xem danh sách các api của một quyền",
            notes = "Mặc định cho phép truy cập với token của user có quyền thuộc loại ROOT",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.ROLE_NOT_FOUND),
            @Response(responseValue = ResponseValue.ROLE_NOT_ALLOWED),
            @Response(responseValue = ResponseValue.INVALID_TOKEN),
            @Response(responseValue = ResponseValue.EXPIRED_TOKEN)
    })
    @RoleBaseAccessControl
    @GetMapping("/roles/{rid}/apis")
    public List<ApiModuleDto> getApisOfRole(@PathVariable(value = "rid") int roleID) throws ResponseException {
        return RBACService.getApisOfRole(roleID);
    }

    @ApiOperation(value = "Phân danh sách api cho một quyền",
            notes = "Mặc định cho phép truy cập với token của user có quyền thuộc loại ROOT. " +
                    "Không thể thực hiện hành động này cho những quyền thuộc loại ROOT",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.ROLE_NOT_FOUND),
            @Response(responseValue = ResponseValue.CANNOT_MODIFY_ROOT_ACCESS_GRANT),
            @Response(responseValue = ResponseValue.ROLE_NOT_ALLOWED),
            @Response(responseValue = ResponseValue.INVALID_TOKEN),
            @Response(responseValue = ResponseValue.EXPIRED_TOKEN)
    })
    @RoleBaseAccessControl
    @PutMapping("/roles/{rid}/apis")
    public void updateApisForRole(@PathVariable(value = "rid") int roleID,
                                          @RequestBody Set<Integer> apiIDs) throws ResponseException {
        RBACService.updateApisForRole(roleID, apiIDs);
    }
}
