package com.spring.baseproject.modules.auth.models.dtos;

import java.util.Map;

public class AuthorizedUser {
    private String userID;
    private String username;

    public AuthorizedUser() {
    }

    public AuthorizedUser(Map<String, ?> map) {
        this((String) map.get("user_id"),
                (String) map.get("user_name"));
    }

    public AuthorizedUser(CustomUserDetail customUserDetail) {
        this(customUserDetail.getUserID(),
                customUserDetail.getUsername());
    }

    public AuthorizedUser(String userID, String username) {
        this.userID = userID;
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
