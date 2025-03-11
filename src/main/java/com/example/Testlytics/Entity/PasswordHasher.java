package com.example.Testlytics.Entity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "securepassword";
        String hashedPassword = encoder.encode(rawPassword);

        System.out.println("Hashed Password: " + hashedPassword);
    }
}
