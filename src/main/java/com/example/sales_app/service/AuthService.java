package com.example.sales_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sales_app.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${activation.url}")
    private String activationUrlBase;

    private final JavaMailSender emailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, JavaMailSender emailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailSender = emailSender;
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

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(User.Role.ADMIN);
        user.setCreatedBy(request.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        return user;
    }

    public UserResponse getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserResponse(user.getUsername(), user.getRole().name(), user.isActive());
    }

    public void sendActivationEmail(String toEmail, String token) {
        String activationUrl = activationUrlBase + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(toEmail);
        message.setSubject("Account Activation");
        message.setText("Please activate your account by clicking the following link: " + activationUrl);

        emailSender.send(message);
    }

    public void generateActivationTokenAndSave(User user) {
        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);
        user.setTokenCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        sendActivationEmail(user.getEmail(), token);
    }
}
