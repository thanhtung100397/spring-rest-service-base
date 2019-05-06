package com.spring.baseproject.modules.demo_auth.controllers;

import com.spring.baseproject.annotations.auth.AuthorizationRequired;
import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo_auth.services.UserProfileService;
import com.spring.baseproject.swagger.demo_auth.UserDtoSwagger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AuthorizationRequired
@RequestMapping("/api/auth-demo/users")
@Api(description = "Thông tin tài khoản")
public class UserProfileController extends BaseRESTController {
    @Autowired
    private UserProfileService userProfileService;

    @ApiOperation(value = "Lấy thông tin tài khoản",
            notes = "Trả về toàn bộ các thông tin tài khoản của người dùng, " +
                    "thực hiện xác thực người dùng bằng access token",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = UserDtoSwagger.class),
            @Response(responseValue = ResponseValue.USER_NOT_FOUND)
    })
    @GetMapping("/info")
    public BaseResponse getUserProfile() {
        return userProfileService.getUserProfile(getAuthorizedUser().getUserID());
    }
}