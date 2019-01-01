package com.spring.baseproject.modules.demo.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.modules.demo.models.dtos.UpdateUserDto;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;
import com.spring.baseproject.modules.demo.services.UserService;
import com.spring.baseproject.swagger.BaseResponseBodySwagger;
import com.spring.baseproject.swagger.base.FieldValidationErrorsSwagger;
import com.spring.baseproject.swagger.demo.demo_controller.UserDtoSwagger;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/demo")
public class DemoController extends BaseRESTController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Get list users", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = UserDtoSwagger.class)
    })
    @GetMapping("/users")
    public BaseResponse getUsers() {
        return userService.getUsers();
    }

    @ApiOperation(value = "Find user by username", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = UserDtoSwagger.class)
    })
    @GetMapping("/users/{username}")
    public BaseResponse getUser(@PathVariable("username") String username) {
        return userService.findUser(username);
    }

    @ApiOperation(value = "Create new user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = BaseResponseBodySwagger.class),
            @Response(responseValue = "USERNAME_EXISTS"),
            @Response(responseValue = "FIELD_VALIDATION_ERROR", responseBody = FieldValidationErrorsSwagger.class)
    })
    @PostMapping("/users")
    public BaseResponse createNewUser(@RequestBody @Valid UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @ApiOperation(value = "Update user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = BaseResponseBodySwagger.class),
            @Response(responseValue = "USER_NOT_FOUND"),
            @Response(responseValue = "FIELD_VALIDATION_ERROR", responseBody = FieldValidationErrorsSwagger.class)
    })
    @PutMapping("/users/{username}")
    public BaseResponse updateUser(@PathVariable("username") String username,
                                   @RequestBody @Valid UpdateUserDto updateUserDto) {
        return userService.updateUser(username, updateUserDto);
    }

    @ApiOperation(value = "Delete user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = BaseResponseBodySwagger.class),
            @Response(responseValue = "USER_NOT_FOUND")
    })
    @DeleteMapping("/users/{username}")
    public BaseResponse deleteUser(@PathVariable("username") String username) {
        return userService.deleteUser(username);
    }
}
