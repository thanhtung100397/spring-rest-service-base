package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.auth.models.dtos.NewUserDto;
import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.User;
import com.spring.baseproject.modules.auth.repositories.RoleRepository;
import com.spring.baseproject.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public BaseResponse registerNewUser(NewUserDto newUserDto) {
        if (userRepository.existsByUsername(newUserDto.getUsername())) {
            return new BaseResponse(ResponseValue.USERNAME_EXISTS);
        }
        Role role = null;
        if (newUserDto.getRoleID() != null) {
            role = roleRepository.findFirstById(newUserDto.getRoleID());
            if (role == null) {
                return new BaseResponse(ResponseValue.ROLE_NOT_FOUND);
            }
        }
        User user = new User();
        user.setUsername(newUserDto.getUsername());
        String hashPassword = passwordEncoder.encode(newUserDto.getPassword());
        user.setPassword(hashPassword);
        user.setRole(role);
        userRepository.save(user);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
