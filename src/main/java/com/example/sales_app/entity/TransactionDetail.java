package com.example.sales_app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transaction_details")
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="transaction_id", nullable=false)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(nullable = false)
    private int quantity = 0;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    private LocalDateTime createdAt;
    private String createdBy;
}
