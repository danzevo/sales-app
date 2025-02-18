package com.example.sales_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sales_app.entity.Product;
import com.example.sales_app.request.ProductRequest;
import com.example.sales_app.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    private MockMvc mockMvc;

    @Mock 
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProduct() throws Exception {
        List<Product> mockProducts = Arrays.<Product>asList(
            new Product(1L, "Product A", 100, BigDecimal.valueOf(100.0), false, LocalDateTime.now(), "Admin", null, null, null, null),
            new Product(2L, "Product B", 150, BigDecimal.valueOf(150.0), false, LocalDateTime.now(), "Admin", null, null, null, null)
        );
        when(productService.getAllProducts()).thenReturn(mockProducts);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[0].stock").value(100))
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    void testAddProductSuccess() throws Exception {
        ProductRequest request = new ProductRequest("New Product", 200, BigDecimal.valueOf(200.0));
        doNothing().when(productService).addProduct(any(ProductRequest.class));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added successfully"));
    }

    @Test
    void testUpdateProductSuccess() throws Exception {
        ProductRequest request = new ProductRequest("Updated Product", 150, BigDecimal.valueOf(150.0));
        doNothing().when(productService).updateProduct(eq(1L), any(ProductRequest.class));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));
    }

    @Test
    void testDeleteProductSuccess() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }
}
