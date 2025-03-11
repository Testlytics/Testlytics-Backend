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

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(@RequestParam(required = false) String role) {
        List<User> users = userService.getAllActiveUsers(role);
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUserWithoutImage)
                .collect(Collectors.toList());

        ApiResponse<List<UserDTO>> response = new ApiResponse<>(
                200, "success", "Users fetched successfully", userDTOs
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Integer id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            UserDTO dto = UserDTO.fromUserWithImage(userOptional.get());
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    200, "success", "User fetched successfully", dto
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    404, "error", "User not found", null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        if (user.getRole() == null || user.getRole().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "error", "Role ID is required", null));
        }
        Optional<Role> roleOptional = roleService.getRoleById(user.getRole().getId());
        if (roleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "error", "Invalid role ID", null));
        }
        user.setRole(roleOptional.get());
        User createdUser = userService.saveUser(user);
        UserDTO dto = UserDTO.fromUserWithoutImage(createdUser);
        ApiResponse<UserDTO> response = new ApiResponse<>(201, "success", "User created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Integer id, @RequestBody User user) {
        if (user.getRole() == null || user.getRole().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "error", "Role ID is required", null));
        }
        Optional<Role> roleOptional = roleService.getRoleById(user.getRole().getId());
        if (roleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "error", "Invalid role ID", null));
        }
        user.setRole(roleOptional.get());
        try {
            User updatedUser = userService.updateUser(id, user);
            UserDTO dto = UserDTO.fromUserWithoutImage(updatedUser);
            return ResponseEntity.ok(new ApiResponse<>(200, "success", "User updated successfully", dto));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "error", ex.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable Integer id) {
        boolean deleted = userService.softDeleteUser(id);
        if (deleted) {
            ApiResponse<Void> response = new ApiResponse<>(200, "success", "User soft deleted successfully", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<Void> response = new ApiResponse<>(404, "error", "User not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // @PutMapping("/{id}/restore")
    // @PreAuthorize("hasAuthority('ADMIN')")
    // public ResponseEntity<ApiResponse<Void>> restoreUser(@PathVariable Integer id) {
    //     boolean restored = userService.restoreUser(id);
    //     if (restored) {
    //         ApiResponse<Void> response = new ApiResponse<>(200, "success", "User restored successfully", null);
    //         return ResponseEntity.ok(response);
    //     }
    //     ApiResponse<Void> response = new ApiResponse<>(404, "error", "User not found or not deleted", null);
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    // }

    @PostMapping("/{userId}/image")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<UserDTO>> uploadImage(@PathVariable Integer userId,
                                                            @RequestParam("image") MultipartFile file) throws IOException {
        User updatedUser = userService.uploadUserImage(userId, file);
        UserDTO dto = UserDTO.fromUserWithImage(updatedUser);
        ApiResponse<UserDTO> response = new ApiResponse<>(200, "success", "Image uploaded successfully", dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/image")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<String>> getImage(@PathVariable Integer userId) {
        try {
            String imageBase64 = userService.getUserImageBase64(userId);
            ApiResponse<String> response = new ApiResponse<>(200, "success", "Image fetched successfully", imageBase64);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ApiResponse<String> response = new ApiResponse<>(404, "error", ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
