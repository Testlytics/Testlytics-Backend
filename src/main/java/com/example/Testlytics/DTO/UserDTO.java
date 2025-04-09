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
    private String role;  // Role name
    private boolean deletedOn; // ✅ Will be true if user is soft-deleted

    // ✅ All-args constructor
    public UserDTO(Integer userId, String name, String email, String image, String role, boolean deletedOn) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.image = image;
        this.role = role;
        this.deletedOn = deletedOn;
    }

    // ✅ No-args constructor
    public UserDTO() {
    }

    // ✅ Factory method to convert User to UserDTO
    public static UserDTO fromUser(User user) {
        String base64Image = null;
        if (user.getImage() != null) {
            base64Image = Base64.getEncoder().encodeToString(user.getImage());
        }

        // ✅ Set deletedOn to true if deletedOn in entity is not null
        boolean isDeleted = user.getDeletedOn() != null;

        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                base64Image,
                user.getRole().getRoleName(),
                isDeleted
        );
    }
}
