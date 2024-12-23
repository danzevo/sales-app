package com.example.sales_app.service;

import org.springframework.stereotype.Service;

import com.example.sales_app.entity.Product;
import com.example.sales_app.entity.Transaction;
import com.example.sales_app.entity.TransactionDetail;
import com.example.sales_app.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.sales_app.repository.ProductRepository;
import com.example.sales_app.repository.TransactionDetailRepository;
import com.example.sales_app.repository.TransactionRepository;
import com.example.sales_app.repository.UserRepository;
import com.example.sales_app.request.TransactionRequest;
import com.example.sales_app.util.SecurityUtil;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public TransactionService(TransactionRepository transactionRepository, 
                                TransactionDetailRepository transactionDetailRepository,
                                UserRepository userRepository,
                                ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void createTransaction(TransactionRequest request) {
        User cashier = userRepository.findByUsername(SecurityUtil.getCurrentUser()).orElseThrow(() -> new IllegalArgumentException("Cashier not found"));
        
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setCashier(cashier);
        transaction.setDate(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setCreatedBy(cashier.getUsername());

        BigDecimal totalPrice = request.getTransactionDetails().stream()
                        .map(detailRequest -> {
                            Product product = productRepository.findById(detailRequest.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));

                            int updateStock = product.getStock() - detailRequest.getQuantity();
                            if(updateStock < 0) {
                                throw new IllegalArgumentException("Insufficient stock for product "+product.getName());
                            }
                            product.setStock(updateStock);
                            product.setChangedBy(SecurityUtil.getCurrentUser());
                            product.setChangedAt(LocalDateTime.now());
                            productRepository.save(product);
                            
                            BigDecimal detailTotal = product.getPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity()));
                            return detailTotal;
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        
        transaction.setTotalPrice(totalPrice);
        transaction.setCreatedBy(SecurityUtil.getCurrentUser());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        request.getTransactionDetails().forEach(detailRequest -> {
            Product product = productRepository.findById(detailRequest.getProductId())
                                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            
            TransactionDetail detail = new TransactionDetail();
            detail.setTransaction(transaction);
            detail.setProduct(product);
            detail.setQuantity(detailRequest.getQuantity());
            detail.setPrice(product.getPrice());
            detail.setCreatedAt(LocalDateTime.now());
            detail.setCreatedBy(cashier.getUsername());

            transactionDetailRepository.save(detail);
        });
    }
}
