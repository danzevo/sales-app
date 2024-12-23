package com.example.sales_app.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionDetailRequest {
    private Long productId;
    private int quantity;
    private BigDecimal price;
}
