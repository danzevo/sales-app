package com.example.sales_app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name="cashier_id", nullable=false)
    private User cashier;

    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransactionDetail> details;

    private LocalDateTime createdAt;
    private String createdBy;
}
