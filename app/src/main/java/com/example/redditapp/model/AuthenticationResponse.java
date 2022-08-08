package com.example.redditapp.model;

import java.util.Date;

public class AuthenticationResponse {
    private String authenticationToken;
    private String username;
    private Date expiresAt;

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "authenticationToken='" + authenticationToken + '\'' +
                '}';
    }
}


