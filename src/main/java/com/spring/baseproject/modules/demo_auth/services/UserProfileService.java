package com.spring.baseproject.modules.demo_auth.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo_auth.models.dtos.UserDto;
import com.spring.baseproject.modules.demo_auth.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public BaseResponse getUserProfile(String userID) {
        UserDto userDto = userProfileRepository.getUserDto(userID);
        if (userDto == null) {
            return new BaseResponse(ResponseValue.USER_NOT_FOUND);
        }
        return new BaseResponse<>(ResponseValue.SUCCESS, userDto);
    }
}
