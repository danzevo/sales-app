package com.example.sales_app.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sales_app.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.sales_app.repository.UserRepository;
import com.example.sales_app.request.LoginRequest;
import com.example.sales_app.request.RegisterRequest;
import com.example.sales_app.response.UserResponse;
import com.example.sales_app.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        
        if(user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            if(!user.get().isActive()) throw new IllegalArgumentException("User not activated");
            String token = jwtUtil.generateToken(user.get().getUsername(), user.get().getRole().name());
            
            return token;
        }
        throw new IllegalArgumentException("Invalid username or password");
    }

    public void register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.KASIR);
        user.setCreatedBy(request.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserResponse getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserResponse(user.getUsername(), user.getRole().name(), user.isActive());
    }
}
