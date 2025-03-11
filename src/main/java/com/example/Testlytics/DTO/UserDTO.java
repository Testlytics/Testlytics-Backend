package com.example.Testlytics.DTO;

import com.example.Testlytics.Entity.User;
import java.util.Base64;

public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String imageBase64; // Stores Base64 string when fetching a single user

    public UserDTO() {}

    // Constructor without image (for listing all users)
    public UserDTO(Integer userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    // Constructor with image (for fetching a single user)
    public UserDTO(Integer userId, String username, String email, String imageBase64) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.imageBase64 = imageBase64;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getImageBase64() {
        return imageBase64;
    }
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    // Convert a User entity to UserDTO without image (for listing all users)
    public static UserDTO fromUserWithoutImage(User user) {
        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail());
    }

    // Convert a User entity to UserDTO with image (for fetching a single user)
    public static UserDTO fromUserWithImage(User user) {
        String imageBase64 = (user.getImage() != null) ? Base64.getEncoder().encodeToString(user.getImage()) : null;
        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), imageBase64);
    }
}
