package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo.models.dtos.NameDto;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public BaseResponse getFullName(String firstName, String lastName) {
        //TODO business logic here
        NameDto nameDto = new NameDto(firstName, lastName);
        nameDto.setFullName(lastName + " " + firstName);
        return new BaseResponse<>(ResponseValue.SUCCESS, nameDto);
    }

    public BaseResponse createNewUser(UserDto userDto) {
        //TODO business logic here
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
