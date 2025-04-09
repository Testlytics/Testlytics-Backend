package com.example.Testlytics.Controller;

import com.example.Testlytics.Config.JwtTokenProvider;
import com.example.Testlytics.Repository.UserRepository;
import com.example.Testlytics.Entity.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")  
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, 
                          JwtTokenProvider jwtTokenProvider, 
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
    
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    
            // Check if user is soft-deleted
            if (user.getDeletedOn() != null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User account is deactivated");
            }
    
            // Authenticate credentials
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            // Generate JWT token
            String token = jwtTokenProvider.createToken(email, user.getRole().getRoleName());
    
            // Return token and role
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole().getRoleName());
            response.put("userId", user.getUserId());
    
            return ResponseEntity.ok(response);
    
        } catch (ResponseStatusException e) {
            throw e; // Return as is
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
    
@PostMapping("/logout")
public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        jwtTokenProvider.blacklistToken(token);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
}


}
