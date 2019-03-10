package com.spring.baseproject.modules.demo.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo.models.dtos.UpdateUserDto;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;
import com.spring.baseproject.modules.demo.services.UserService;
import com.spring.baseproject.swagger.demo.demo_controller.ListUserDtosSwagger;
import com.spring.baseproject.swagger.demo.demo_controller.UserDtoSwagger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/demo")
@Api(description = "Demo REST API")
public class DemoFakeController extends BaseRESTController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Lấy danh sách user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ListUserDtosSwagger.class)
    })
    @GetMapping("/users")
    public BaseResponse getUsers() {
        return userService.getUsers();
    }

    @ApiOperation(value = "Lấy user bằng username", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = UserDtoSwagger.class),
            @Response(responseValue = ResponseValue.USER_NOT_FOUND)
    })
    @GetMapping("/users/{username}")
    public BaseResponse getUser(@PathVariable("username") String username) {
        return userService.findUser(username);
    }

    @ApiOperation(value = "Tạo mới user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.USERNAME_EXISTS)
    })
    @PostMapping("/users")
    public BaseResponse createNewUser(@RequestBody @Valid UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @ApiOperation(value = "Cập nhật user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.USER_NOT_FOUND)
    })
    @PutMapping("/users/{username}")
    public BaseResponse updateUser(@PathVariable("username") String username,
                                   @RequestBody @Valid UpdateUserDto updateUserDto) {
        return userService.updateUser(username, updateUserDto);
    }

    @ApiOperation(value = "Xóa user bằng username", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/users/{username}")
    public BaseResponse deleteUser(@PathVariable("username") String username) {
        return userService.deleteUser(username);
    }

    @ApiOperation(value = "Xóa danh sách user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/users")
    public BaseResponse deleteUsers(@RequestBody Set<String> listUsername) {
        return userService.deleteUsers(listUsername);
    }
}
