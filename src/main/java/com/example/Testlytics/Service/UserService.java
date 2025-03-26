package com.example.Testlytics.Service;


import com.example.Testlytics.Entity.User;
import com.example.Testlytics.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public UserService(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

   
    

    /**
     * Generates a unique 4-digit user ID.
     */
    private Integer generateUniqueUserId() {
        int userId;
        do {
            userId = 1000 + random.nextInt(9000);
        } while (userRepository.existsById(userId));
        return userId;
    }

    /**
     * Create a new user (with optional image).
     */
    public User saveUser(User user) {
        if (user.getUserId() == null) {
            user.setUserId(generateUniqueUserId());
        }
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Fetch all active users (ignoring role filtering).
     */
    @Transactional
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }

    /**
     * Get a user by ID.
     */
    @Transactional
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findByIdIfNotDeleted(userId);
    }

    /**
     * Update an existing user (with optional image update).
     */
    public User updateUser(Integer id, User updatedUser, byte[] imageData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (imageData != null) {
            existingUser.setImage(imageData);
        }
        return userRepository.save(existingUser);
    }

    /**
     * Soft delete a user.
     */
    public boolean softDeleteUser(Integer userId) {
        return userRepository.findByIdIfNotDeleted(userId)
                .map(user -> {
                    user.softDelete();
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
