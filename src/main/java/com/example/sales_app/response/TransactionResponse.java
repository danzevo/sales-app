package com.example.sales_app.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class TransactionResponse {
    private UUID transactionId;
    private String cashierName;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private List<TransactionDetailResponse> details;
}
