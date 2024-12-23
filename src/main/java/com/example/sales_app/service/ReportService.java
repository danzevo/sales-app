package com.example.sales_app.service;

import org.springframework.stereotype.Service;

import com.example.sales_app.entity.Transaction;
import com.example.sales_app.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.sales_app.repository.TransactionRepository;
import com.example.sales_app.request.ReportRequest;
import com.example.sales_app.response.TransactionDetailResponse;
import com.example.sales_app.response.TransactionResponse;

@Service
public class ReportService {
    private final TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<?> generateReport(ReportRequest request) {
        if(request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        List<Transaction> transactions = transactionRepository.findByDateRange(request.getStartDate(), request.getEndDate());

        return transactions.stream().map(transaction -> {
                    TransactionResponse response = new TransactionResponse();
                    response.setTransactionId(transaction.getId());
                    response.setCashierName(transaction.getCashier().getUsername());
                    response.setDate(transaction.getDate());
                    response.setTotalPrice(transaction.getTotalPrice());

                    List<TransactionDetailResponse> detailResponses = transaction.getDetails().stream().map( detail -> {
                        TransactionDetailResponse detailResponse = new TransactionDetailResponse();
                        detailResponse.setProductName(detail.getProduct().getName());
                        detailResponse.setPrice(detail.getPrice());
                        detailResponse.setQuantity(detail.getQuantity());
                        return detailResponse;
                    }).collect(Collectors.toList());

                    response.setDetails(detailResponses);
                    return response;
        }).toList();
    }
}
