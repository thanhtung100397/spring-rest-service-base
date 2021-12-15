package com.spring.baseproject.modules.auth.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.auth.models.dtos.AuthenticationResult;
import com.spring.baseproject.modules.auth.models.dtos.RefreshTokenDto;
import com.spring.baseproject.modules.auth.models.dtos.UsernamePasswordDto;
import com.spring.baseproject.modules.auth.services.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authentication")
@Api(description = "Xác thực người dùng")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @ApiOperation(value = "Xác thực bằng người dùng bằng username và password",
            notes = "Xác thực thông qua username và password của người dùng, " +
                    "yêu cầu thêm client_id và secret của ứng dụng thực hiện gọi api",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.WRONG_CLIENT_ID_OR_SECRET),
            @Response(responseValue = ResponseValue.WRONG_USERNAME_OR_PASSWORD)
    })
    @PostMapping("/username-password")
    public AuthenticationResult authenticateByUsernamePassword(
            @RequestHeader(value = "client_id") String clientID,
            @RequestHeader(value = "secret") String secret,
            @RequestBody @Valid UsernamePasswordDto usernamePassword
    ) throws Exception {
        return authenticationService.authenticateByUsernameAndPassword(clientID, secret, usernamePassword);
    }

    @ApiOperation(value = "Xác thực người dùng bằng refresh token",
            notes = "Xác thực thông qua refresh token của người dùng, " +
                    "yêu cầu thêm client_id và secret của ứng dụng thực hiện gọi api",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.WRONG_CLIENT_ID_OR_SECRET),
            @Response(responseValue = ResponseValue.INVALID_TOKEN),
            @Response(responseValue = ResponseValue.EXPIRED_TOKEN)
    })
    @PostMapping("/refresh-token")
    public AuthenticationResult authenticateByRefreshToken(
            @RequestHeader(value = "client_id") String clientID,
            @RequestHeader(value = "secret") String secret,
            @RequestBody @Valid RefreshTokenDto refreshToken
    ) throws Exception {
        return authenticationService.authenticateByRefreshToken(clientID, secret, refreshToken);
    }
}