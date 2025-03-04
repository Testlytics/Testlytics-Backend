package com.example.Testlytics.Controller;

import com.example.Testlytics.Entity.User;
import com.example.Testlytics.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers(@RequestParam(required = false) String role) {
        return userService.getAllActiveUsers(role); // Fetch only active users
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam String roleName) {
        User createdUser = userService.saveUser(user, roleName);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user, @RequestParam String roleName) {
        User updatedUser = userService.updateUser(id, user, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    // Soft delete a user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Integer id) {
        boolean deleted = userService.softDeleteUser(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Restore a soft-deleted user
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> restoreUser(@PathVariable Integer id) {
        boolean restored = userService.restoreUser(id);
        if (restored) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/{userId}/upload-image")
    public User uploadImage(@PathVariable Integer userId, @RequestParam("image") MultipartFile file) throws IOException {
        return userService.uploadUserImage(userId, file);
    }

    @GetMapping("/{userId}/image")
    public String getImage(@PathVariable Integer userId) {
        return userService.getUserImageBase64(userId);
    }
}
