package com.spring.baseproject.modules.demo.models.entities;

import com.spring.baseproject.modules.demo.models.dtos.UpdateUserDto;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;

public class User {
    private String username;
    private String password;
    private String email;
    private String phone;

    public User() {
    }

    public User(UserDto userDto) {
        this.username = userDto.getUsername();
        update(userDto);
    }

    public void update(UpdateUserDto updateUserDto) {
        this.password = updateUserDto.getPassword();
        this.email = updateUserDto.getEmail();
        this.phone = updateUserDto.getPhone();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
