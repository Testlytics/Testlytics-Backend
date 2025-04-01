package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.UserDTO;
import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Entity.User;
import com.example.Testlytics.Service.RoleService;
import com.example.Testlytics.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    // Get all users (ignoring any role filtering)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllActiveUsers()
                .stream()
                .map(UserDTO::fromUser)  // Using factory method to convert User to UserDTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(200, "success", "Users fetched successfully", userDTOs));
    }

    // Get a user by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new ApiResponse<>(200, "success", "User fetched successfully", UserDTO.fromUser(user))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "error", "User not found", null)));
    }

    // Create a new user (with optional image upload)
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
public ResponseEntity<ApiResponse<UserDTO>> createUser(
        @RequestParam("user") String userJson,
        @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    User user = objectMapper.readValue(userJson, User.class); // Deserialize JSON

    // ✅ Validate Required Fields
    if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Username is required", null));
    }
    if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Email is required", null));
    }
    if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Password is required", null));
    }
    if (user.getRole() == null || user.getRole().getId() == null) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Role ID is required", null));
    }

    // ✅ Check if email already exists
    if (userService.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Email is already taken", null));
    }
   
    // ✅ Check if role exists
    Optional<Role> roleOptional = roleService.getRoleById(user.getRole().getId());
    if (roleOptional.isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Invalid role ID", null));
    }

    // Set role
    user.setRole(roleOptional.get());

    // ✅ Process Image
    if (image != null && !image.isEmpty()) {
        user.setImage(image.getBytes());
    }

    // ✅ Save User and Handle Unique Constraint Violations
    try {
        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "success", "User created successfully", UserDTO.fromUser(createdUser)));
    } catch (DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "error", "Email already exists", null));
    }
}
    
    // Update user details (including optional image update)
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Integer id,
            @RequestParam("user") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class); // Deserialize JSON to User

        byte[] imageData = (image != null && !image.isEmpty()) ? image.getBytes() : null;

        try {
            User updatedUser = userService.updateUser(id, user, imageData);
            return ResponseEntity.ok(new ApiResponse<>(200, "success", "User updated successfully", UserDTO.fromUser(updatedUser)));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "error", ex.getMessage(), null));
        }
    }

    // Soft delete a user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable Integer id) {
        boolean deleted = userService.softDeleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(200, "success", "User soft deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "error", "User not found", null));
    }
}
