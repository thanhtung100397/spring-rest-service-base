package com.spring.baseproject.modules.auth.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import com.spring.baseproject.modules.auth.models.dtos.NewRoleDto;
import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import com.spring.baseproject.modules.auth.services.RoleService;
import com.spring.baseproject.swagger.auth.role.PageRoleDtoSwagger;
import com.spring.baseproject.swagger.auth.role.RoleDtoSwagger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@Api(description = "Quyền")
public class RoleController extends BaseRESTController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "Xem danh sách", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = PageRoleDtoSwagger.class)
    })
    @GetMapping
    public BaseResponse getRoles(@RequestParam(value = StringConstants.SORT_BY, defaultValue = Role.NAME + "," + Role.TYPE) List<String> sortBy,
                                 @RequestParam(value = StringConstants.SORT_TYPE, defaultValue = "asc, asc") List<String> sortType,
                                 @RequestParam(value = StringConstants.PAGE_INDEX, defaultValue = "0") int pageIndex,
                                 @RequestParam(value = StringConstants.PAGE_SIZE, defaultValue = "0") int pageSize,
                                 @RequestParam(value = "type", required = false) RoleType roleType) {
        return roleService.getRoles(roleType, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem chi tiết", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = RoleDtoSwagger.class),
            @Response(responseValue = ResponseValue.ROLE_NOT_FOUND)
    })
    @GetMapping("/{rid}")
    public BaseResponse getRole(@PathVariable("rid") int roleID) {
        return roleService.getRole(roleID);
    }

    @ApiOperation(value = "Tạo mới", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = RoleDtoSwagger.class),
            @Response(responseValue = ResponseValue.ROLE_EXISTS)
    })
    @PostMapping
    public BaseResponse createNewRole(@Valid @RequestBody NewRoleDto newRoleDto) {
        return roleService.createNewRole(newRoleDto);
    }

    @ApiOperation(value = "Cập nhật", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = RoleDtoSwagger.class),
            @Response(responseValue = ResponseValue.ROLE_NOT_FOUND)
    })
    @PutMapping("/{rid}")
    public BaseResponse updateRole(@PathVariable("rid") int roleID,
                                   @Valid @RequestBody NewRoleDto newRoleDto) {
        return roleService.updateRole(roleID, newRoleDto);
    }

    @ApiOperation(value = "Xóa", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping()
    public BaseResponse deleteRoles(@RequestBody Set<Integer> roleIDs) {
        return roleService.deleteRoles(roleIDs);
    }
}
