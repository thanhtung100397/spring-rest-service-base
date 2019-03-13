package com.spring.baseproject.modules.auth.models.dtos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetail extends User {
    private String userID;

    public CustomUserDetail(String userID, String username, String password,
                            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
