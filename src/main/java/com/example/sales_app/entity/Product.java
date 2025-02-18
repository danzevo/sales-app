package com.example.sales_app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private int stock = 0;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
    
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime changedAt;
    private String changedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
