package com.example.sales_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="is_active", nullable = false)
    private boolean isActive = false;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime changedAt;
    private String changedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    public enum Role {
        ADMIN, KASIR
    }
}
