package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Entity.User;
import com.example.Testlytics.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService; // Role management
    private final BCryptPasswordEncoder passwordEncoder; // Password encoder
    private final Random random = new Random();

    public UserService(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // Generate a unique 4-digit user ID (1000-9999)
    private Integer generateUniqueUserId() {
        int userId;
        do {
            userId = 1000 + random.nextInt(9000);
        } while (userRepository.existsById(userId));
        return userId;
    }

    // Save User with Password Hashing
    public User saveUser(User user) {
        if (user.getUserId() == null) {
            user.setUserId(generateUniqueUserId()); // Assign unique ID
        }

        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Get all active users (filtered by role if provided)
    @Transactional
    public List<User> getAllActiveUsers(String role) {
        if (role != null) {
            return userRepository.findAllActiveUsersByRole(role);
        }
        return userRepository.findAllActiveUsers();
    }

    // Get user by ID (Only active users)
    @Transactional 
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findByIdIfNotDeleted(userId);
    }

    public User updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        // Update fields if provided
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        
    
        // Update role if provided
        if (updatedUser.getRole() != null && updatedUser.getRole().getId() != null) {
            Optional<Role> roleOptional = roleService.getRoleById(updatedUser.getRole().getId());
            if (roleOptional.isPresent()) {
                existingUser.setRole(roleOptional.get());
            } else {
                throw new RuntimeException("Invalid role ID");
            }
        }
    
        return userRepository.save(existingUser);
    }
    
    

    // Soft delete user
    public boolean softDeleteUser(Integer userId) {
        Optional<User> userOptional = userRepository.findByIdIfNotDeleted(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.softDelete();
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Restore user
    // public boolean restoreUser(Integer userId) {
    //     Optional<User> userOptional = userRepository.findById(userId);
    //     if (userOptional.isPresent() && userOptional.get().getDeletedOn() != null) {
    //         User user = userOptional.get();
    //         user.restore();
    //         userRepository.save(user);
    //         return true;
    //     }
    //     return false;
    // }

    // Upload User Image (must be transactional for LOB access)
    @Transactional
    public User uploadUserImage(Integer userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setImage(file.getBytes()); // Convert MultipartFile to byte[]
        return userRepository.save(user);
    }

    // Get User Image as Base64 String (read-only transaction)
    @Transactional(readOnly = true)
    public String getUserImageBase64(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (user.getImage() != null) {
            return Base64.getEncoder().encodeToString(user.getImage());
        }
        throw new RuntimeException("User image not found!");
    }
}
