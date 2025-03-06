package com.example.Testlytics.Controller;

import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Entity.User;
import com.example.Testlytics.Service.RoleService;
import com.example.Testlytics.Service.UserService;
import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(@RequestParam(required = false) String role) {
        List<User> users = userService.getAllActiveUsers(role);
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
        ApiResponse<List<UserDTO>> response = new ApiResponse<>("success", "Users fetched successfully", userDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Integer id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            UserDTO dto = UserDTO.fromUser(userOptional.get());
            ApiResponse<UserDTO> response = new ApiResponse<>("success", "User fetched successfully", dto);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDTO> response = new ApiResponse<>("error", "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @PostMapping
public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
    if (user.getRole() == null || user.getRole().getId() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("error", "Role ID is required", null));
    }

    Optional<Role> roleOptional = roleService.getRoleById(user.getRole().getId()); // Fix this
    if (roleOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("error", "Invalid role ID", null));
    }

    user.setRole(roleOptional.get());
    User createdUser = userService.saveUser(user);
    UserDTO dto = UserDTO.fromUser(createdUser);
    ApiResponse<UserDTO> response = new ApiResponse<>("success", "User created successfully", dto);
    return ResponseEntity.ok(response);
}


@PutMapping("/{id}")
public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Integer id, 
                                                       @RequestBody User user) {
    // Ensure role ID is provided
    if (user.getRole() == null || user.getRole().getId() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("error", "Role ID is required", null));
    }

    // Fetch the role by ID
    Optional<Role> roleOptional = roleService.getRoleById(user.getRole().getId());
    if (roleOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("error", "Invalid role ID", null));
    }

    user.setRole(roleOptional.get());

    try {
        User updatedUser = userService.updateUser(id, user);
        UserDTO dto = UserDTO.fromUser(updatedUser);
        return ResponseEntity.ok(new ApiResponse<>("success", "User updated successfully", dto));
    } catch (RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("error", ex.getMessage(), null));
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable Integer id) {
        boolean deleted = userService.softDeleteUser(id);
        if (deleted) {
            ApiResponse<Void> response = new ApiResponse<>("success", "User soft deleted successfully", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<Void> response = new ApiResponse<>("error", "User not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreUser(@PathVariable Integer id) {
        boolean restored = userService.restoreUser(id);
        if (restored) {
            ApiResponse<Void> response = new ApiResponse<>("success", "User restored successfully", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<Void> response = new ApiResponse<>("error", "User not found or not deleted", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/{userId}/upload-image")
    public ResponseEntity<ApiResponse<UserDTO>> uploadImage(@PathVariable Integer userId,
                                                            @RequestParam("image") MultipartFile file) throws IOException {
        User updatedUser = userService.uploadUserImage(userId, file);
        UserDTO dto = UserDTO.fromUser(updatedUser);
        ApiResponse<UserDTO> response = new ApiResponse<>("success", "Image uploaded successfully", dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/image")
    public ResponseEntity<ApiResponse<String>> getImage(@PathVariable Integer userId) {
        try {
            String imageBase64 = userService.getUserImageBase64(userId);
            ApiResponse<String> response = new ApiResponse<>("success", "Image fetched successfully", imageBase64);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ApiResponse<String> response = new ApiResponse<>("error", ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
