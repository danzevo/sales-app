package com.example.sales_app.request;

import java.util.List;

import lombok.Data;

@Data
public class TransactionRequest {
    private List<TransactionDetailRequest> transactionDetails;
}
