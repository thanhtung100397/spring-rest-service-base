package com.spring.baseproject.modules.demo_auth.controllers;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_auth.models.dtos.UserDto;
import com.spring.baseproject.modules.demo_auth.services.UserProfileService;
import com.spring.baseproject.utils.auth.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AuthorizationRequired
@RequestMapping("/api/auth-demo/users")
@Api(description = "Thông tin tài khoản")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @ApiOperation(value = "Lấy thông tin tài khoản",
            notes = "Trả về toàn bộ các thông tin tài khoản của người dùng, " +
                    "thực hiện xác thực người dùng bằng access token",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.USER_NOT_FOUND)
    })
    @GetMapping("/info")
    public UserDto getUserProfile() throws ResponseException {
        return userProfileService.getUserProfile(AuthUtils.getAuthorizedUser().getUserID());
    }
}