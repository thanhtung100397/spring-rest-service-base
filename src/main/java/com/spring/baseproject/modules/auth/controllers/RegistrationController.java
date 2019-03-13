package com.spring.baseproject.modules.auth.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.auth.models.dtos.NewUserDto;
import com.spring.baseproject.modules.auth.services.UserRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/registration")
@Api(description = "Tạo tài khoản người dùng")
public class RegistrationController extends BaseRESTController {
    @Autowired
    private UserRegistrationService userRegistrationService;

    @ApiOperation(value = "Tạo mới một tài khoản người dùng",
            notes = "Tạo mới một tài khoản người dùng, có kiểm tra username tồn tại, " +
                    "mật khẩu người dùng sẽ được hash trước khi persist\n" +
                    "Lưu ý: với mục đích phát triển, mật khẩu sẽ không được hashing (noop)",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.USERNAME_EXISTS),
            @Response(responseValue = ResponseValue.ROLE_NOT_FOUND)
    })
    @PostMapping()
    public BaseResponse registerNewUser(@RequestBody @Valid NewUserDto newUserDto) {
        return userRegistrationService.registerNewUser(newUserDto);
    }
}
