package com.example.Testlytics.DTO;

import com.example.Testlytics.Entity.User;

public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String password; // Added password field
    private byte[] image;

    public UserDTO() {}

    public UserDTO(Integer userId, String username, String email, String password, byte[] image) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    // Convert a User entity to UserDTO
    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(), // Include password
                user.getImage()
        );
    }
}
