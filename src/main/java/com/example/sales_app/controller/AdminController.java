package com.example.sales_app.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sales_app.entity.User;
import com.example.sales_app.repository.UserRepository;
import com.example.sales_app.util.SecurityUtil;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @PutMapping("/activate/{username}")
    public ResponseEntity<?> activateUser(@PathVariable String username) {
        try{
            User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
            user.setActive(true);        
            user.setChangedBy(SecurityUtil.getCurrentUser());
            user.setChangedAt(LocalDateTime.now());
            userRepository.save(user);

            return ResponseEntity.ok("User activated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: "+e.getMessage());
        }
    }
}
