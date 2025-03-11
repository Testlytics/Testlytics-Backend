package com.example.Testlytics.DTO;

import com.example.Testlytics.Entity.User;
import lombok.Getter;
import lombok.Setter;
import java.util.Base64;

@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String image; // Store image as Base64 string

    // ✅ Constructor
    public UserDTO(Integer userId, String name, String email, String image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.image = image;
    }

    // ✅ No-args constructor (IMPORTANT for Jackson)
    public UserDTO() {
    }

    // ✅ **Factory Method to Convert `User` to `UserDTO`**
    public static UserDTO fromUser(User user) {
        String base64Image = null;
        if (user.getImage() != null) {
            base64Image = Base64.getEncoder().encodeToString(user.getImage());
        }
        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), base64Image);
    }
}
