package com.spring.baseproject.modules.demo_rbac.controllers;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.annotations.rbac.RoleBaseAccessControl;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AuthorizationRequired
@Api(description = "Truy cập thông tin sử dụng Role Base Access Control")
@RequestMapping("/api/rbac-demo")
public class RoleBaseAccessController {

    @ApiOperation(value = "Api chỉ cho phép user có quyền ADMIN truy cập",
            notes = "User có quyền ROOT mặc định có thể truy cập mọi api và không cần cấp quyền",
            response = Iterable.class)
    @Responses(value = {
    })
    @RoleBaseAccessControl(defaultAccess = RoleType.ADMIN)
    @GetMapping("/for-admin")
    public String forAdmin() {
        return"Hello, admin";
    }

    @ApiOperation(value = "Api chỉ cho phép user có quyền VIEWER truy cập",
            notes = "User có quyền ROOT mặc định có thể truy cập mọi api và không cần cấp quyền",
            response = Iterable.class)
    @Responses(value = {
    })
    @RoleBaseAccessControl(defaultAccess = RoleType.VIEWER)
    @GetMapping("/for-viewer")
    public String forViewer() {
        return"Hello, viewer";
    }
}
