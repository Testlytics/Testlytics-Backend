package com.example.Testlytics.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.Testlytics.Repository.UserRepository;

@Configuration
public class SecurityConfig {

    // Bean for your custom JWT authentication filter
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for testing purposes; enable it in production as needed.
            .csrf(csrf -> csrf.disable())
            // Configure endpoint authorization
            .authorizeHttpRequests(auth -> auth
                // Permit unauthenticated access to auth endpoints (e.g., login, register)
                .requestMatchers("/api/auth/**").permitAll()
                // Permit access to role endpoints if needed (for seeding or public role data)
                .requestMatchers("/api/roles/**").permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            // Add your custom JWT filter before the standard authentication filter
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }

    // Expose AuthenticationManager to be used in your authentication endpoints (if needed)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword()) // Ensure password is encoded
                        .authorities("ROLE_" + user.getRole().getRoleName()) // Changed to roleName
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
