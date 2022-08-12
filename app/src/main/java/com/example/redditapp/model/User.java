package com.example.redditapp.model;

import java.time.LocalDate;

public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String description;
    private String displayName;

    public User() {
    }

    public User(String username, String password, String email, String role, String description, String displayName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
        this.displayName = displayName;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email,String role, Long id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
