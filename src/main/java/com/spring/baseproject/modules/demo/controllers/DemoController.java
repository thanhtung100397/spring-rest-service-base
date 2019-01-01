package com.spring.baseproject.modules.demo.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;
import com.spring.baseproject.modules.demo.services.DemoService;
import com.spring.baseproject.swagger.BaseResponseBodySwagger;
import com.spring.baseproject.swagger.base.FieldValidationErrorsSwagger;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/demo")
public class DemoController extends BaseRESTController {
    @Autowired
    private DemoService demoService;

    @ApiOperation(value = "Get full name", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = BaseResponseBodySwagger.class)
    })
    @GetMapping()
    public BaseResponse getFullName(@RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName) {
        return demoService.getFullName(firstName, lastName);
    }

    @ApiOperation(value = "Create new user", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = "SUCCESS", responseBody = BaseResponseBodySwagger.class),
            @Response(responseValue = "FIELD_VALIDATION_ERROR", responseBody = FieldValidationErrorsSwagger.class)
    })
    @PostMapping("/users")
    public BaseResponse createNewUser(@RequestBody @Valid UserDto userDto) {
        return demoService.createNewUser(userDto);
    }
}
