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
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Admin-only: Get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(@RequestParam(required = false) String role) {
        List<User> users = userService.getAllActiveUsers(role);
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
        ApiResponse<List<UserDTO>> response = new ApiResponse<>("success", "Users fetched successfully", userDTOs);
        return ResponseEntity.ok(response);
    }
 
    // Admin-only: Get a user by ID
    @PreAuthorize("hasRole('ADMIN')")
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

    // Admin-only: Create a new user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        if (user.getRole() == null || user.getRole().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Role ID is required", null));
        }
        Optional<Role> roleOptional = roleService.getRoleById(user.getRole().getId());
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

    // Accessible by Admin and Student: Update user details
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Integer id, @RequestBody User user) {
        if (user.getRole() == null || user.getRole().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Role ID is required", null));
        }
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

    // Admin-only: Soft delete a user
    @PreAuthorize("hasRole('ADMIN')")
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

    // Admin-only: Restore a soft-deleted user
    @PreAuthorize("hasRole('ADMIN')")
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

    // Accessible by Admin and Student: Upload user image
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @PostMapping("/{userId}/image")
    public ResponseEntity<ApiResponse<UserDTO>> uploadImage(@PathVariable Integer userId,
                                                            @RequestParam("image") MultipartFile file) throws IOException {
        User updatedUser = userService.uploadUserImage(userId, file);
        UserDTO dto = UserDTO.fromUser(updatedUser);
        ApiResponse<UserDTO> response = new ApiResponse<>("success", "Image uploaded successfully", dto);
        return ResponseEntity.ok(response);
    }

    // Accessible by Admin and Student: Get user image as Base64 string
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
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

    // Accessible by Admin and Student: Update user image
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @PutMapping("/{userId}/image")
    public ResponseEntity<ApiResponse<UserDTO>> updateImage(@PathVariable Integer userId,
                                                            @RequestParam("image") MultipartFile file) throws IOException {
        User updatedUser = userService.uploadUserImage(userId, file);
        UserDTO dto = UserDTO.fromUser(updatedUser);
        ApiResponse<UserDTO> response = new ApiResponse<>("success", "Image updated successfully", dto);
        return ResponseEntity.ok(response);
    }
}
