package com.example.sales_app.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionDetailResponse {
    private String productName;
    private BigDecimal price;
    private int quantity;
}
