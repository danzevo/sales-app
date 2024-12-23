package com.example.sales_app.service;

import org.springframework.stereotype.Service;

import com.example.sales_app.entity.User;
import java.util.Optional;
import com.example.sales_app.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
