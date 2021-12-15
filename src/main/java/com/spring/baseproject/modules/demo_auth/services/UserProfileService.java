package com.spring.baseproject.modules.demo_auth.services;

import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_auth.models.dtos.UserDto;
import com.spring.baseproject.modules.demo_auth.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserDto getUserProfile(String userID) throws ResponseException {
        UserDto userDto = userProfileRepository.getUserDto(userID);
        if (userDto == null) {
            throw new ResponseException(ResponseValue.USER_NOT_FOUND);
        }
        return userDto;
    }
}
