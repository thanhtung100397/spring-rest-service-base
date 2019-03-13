package com.spring.baseproject.modules.auth.models.dtos;

import java.util.Map;

public class AuthorizedUser {
    private String userID;
    private String username;
    private String jti;
    private String clientID;

    public AuthorizedUser() {
    }

    public AuthorizedUser(Map<String, ?> map) {
        this.userID = (String) map.get("user_id");
        this.username = (String) map.get("user_name");
        this.jti = (String) map.get("jti");
        this.clientID = (String) map.get("client_id");
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

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
