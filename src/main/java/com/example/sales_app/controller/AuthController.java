package com.example.sales_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sales_app.entity.User;
import com.example.sales_app.repository.UserRepository;
import com.example.sales_app.request.LoginRequest;
import com.example.sales_app.request.RegisterRequest;
import com.example.sales_app.response.UserResponse;
import com.example.sales_app.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try{
            String token = authService.login(request);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: "+e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try{
            User user = authService.register(request);
            authService.generateActivationTokenAndSave(user);
            return ResponseEntity.ok("User registered successfully. Please check your email to activate your account.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: "+e.getMessage());
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token) {
        try {
            User user = userRepository.findByActivationToken(token)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

            user.setActive(true);
            user.setActivationToken(null);
            userRepository.save(user);

            return ResponseEntity.ok("Account successfully activated!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: "+e.getMessage());
        }
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        try{
            UserResponse userResponse = authService.getCurrentUser();
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
