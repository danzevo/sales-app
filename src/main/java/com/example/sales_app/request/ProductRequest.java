package com.example.sales_app.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private int stock;
    private BigDecimal price;
}
