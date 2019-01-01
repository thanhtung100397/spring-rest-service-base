package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo.models.dtos.UpdateUserDto;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;
import com.spring.baseproject.modules.demo.models.entities.User;
import com.spring.baseproject.modules.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    @Autowired
    private UserRepository userRepository;

    public BaseResponse getUsers() {
        //TODO business logic here
        return new BaseResponse<>(ResponseValue.SUCCESS, userRepository.getUsers());
    }

    public BaseResponse findUser(String username) {
        //TODO business logic here
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return new BaseResponse(ResponseValue.USER_NOT_FOUND);
        }
        return new BaseResponse<>(ResponseValue.SUCCESS, user);
    }

    public BaseResponse createNewUser(UserDto userDto) {
        //TODO business logic here
        User user = userRepository.getUserByUsername(userDto.getUsername());
        if (user != null) {
            return new BaseResponse(ResponseValue.USERNAME_EXISTS);
        }
        User newUser = new User(userDto);
        userRepository.saveUser(newUser);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse updateUser(String username, UpdateUserDto updateUserDto) {
        //TODO business logic here
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            return new BaseResponse(ResponseValue.USERNAME_EXISTS);
        }
        user.setPassword(updateUserDto.getPassword());
        user.setEmail(updateUserDto.getEmail());
        user.setPhone(updateUserDto.getPhone());
        userRepository.saveUser(user);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse deleteUser(String username) {
        //TODO business logic here
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return new BaseResponse(ResponseValue.USER_NOT_FOUND);
        }
        userRepository.deleteUser(username);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
