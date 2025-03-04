package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Entity.User;
import com.example.Testlytics.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService; // Inject RoleService
    private final Random random = new Random();

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    // Generate a unique 4-digit user ID
    private Integer generateUniqueUserId() {
        int userId;
        do {
            userId = 1000 + random.nextInt(9000); // Generate between 1000-9999
        } while (userRepository.existsById(userId)); // Ensure uniqueness
        return userId;
    }

    // Save a new user with role integration
    public User saveUser(User user, String roleName) {
        user.setUserId(generateUniqueUserId()); // Assign unique 4-digit ID
        
        // Fetch role from RoleService
        Optional<Role> roleOptional = roleService.getRoleByName(roleName);
        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Role not found: " + roleName);
        }
        
        user.setRole(roleOptional.get()); // Assign the role
        return userRepository.save(user);
    }

    // Get all active (non-deleted) users
public List<User> getAllActiveUsers(String role) {
    if (role != null) {
        return userRepository.findAllActiveUsersByRole(role);
    }
    return userRepository.findAllActiveUsers();
}

// Get user by ID (Only active users)
public Optional<User> getUserById(Integer userId) {
    return userRepository.findByIdIfNotDeleted(userId);
}


    // Update user details with role integration
    public User updateUser(Integer userId, User updatedUser, String roleName) {
        Optional<User> userOptional = userRepository.findByIdIfNotDeleted(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());

            // Fetch and update role
            Optional<Role> roleOptional = roleService.getRoleByName(roleName);
            if (roleOptional.isEmpty()) {
                throw new RuntimeException("Role not found: " + roleName);
            }
            user.setRole(roleOptional.get());

            user.setModifiedOn(LocalDateTime.now()); // Update timestamp
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found!");
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
    public boolean restoreUser(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getDeletedOn() != null) {
            User user = userOptional.get();
            user.restore();
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public User uploadUserImage(Integer userId, MultipartFile file) throws IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setImage(file.getBytes()); // Convert MultipartFile to byte[]
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found!");
    }
    public String getUserImageBase64(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getImage() != null) {
            return Base64.getEncoder().encodeToString(userOptional.get().getImage());
        }
        throw new RuntimeException("User image not found!");
    }
}
