package com.example.sales_app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sales_app.request.TransactionDetailRequest;
import com.example.sales_app.request.TransactionRequest;
import com.example.sales_app.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testCreateTransactionSuccess() throws Exception {
        TransactionDetailRequest detailRequest = new TransactionDetailRequest();
        detailRequest.setProductId(1L);
        detailRequest.setQuantity(2);
        detailRequest.setPrice(new BigDecimal("100.50"));

        TransactionRequest request = new TransactionRequest();
        request.setTransactionDetails(Arrays.asList(detailRequest));

        doNothing().when(transactionService).createTransaction(any(TransactionRequest.class));

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction created successfully"));
    }

    @Test
    void testCreateTransactionBadRequest() throws Exception {
        TransactionRequest request = new TransactionRequest();

        doThrow(new IllegalArgumentException("Invalid transaction data"))
            .when(transactionService).createTransaction(any(TransactionRequest.class));

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid transaction data"));
        
        verify(transactionService, times(1)).createTransaction(any(TransactionRequest.class));
    }

    @Test
    void testCreateTransactionInternalServerError() throws Exception {
        TransactionRequest request = new TransactionRequest();

        doThrow(new RuntimeException("Database Error"))
            .when(transactionService).createTransaction(any(TransactionRequest.class));

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Database Error"));
        
        verify(transactionService, times(1)).createTransaction(any(TransactionRequest.class));
    }
}
