package com.example.sales_app.service;

import org.springframework.stereotype.Service;

import com.example.sales_app.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import com.example.sales_app.repository.ProductRepository;
import com.example.sales_app.request.ProductRequest;
import com.example.sales_app.util.SecurityUtil;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllNonDeleted();
    }

    public void addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setCreatedBy(SecurityUtil.getCurrentUser());
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setName(request.getName());
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setChangedBy(SecurityUtil.getCurrentUser());
        product.setChangedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setDeletedBy(SecurityUtil.getCurrentUser());
        product.setDeletedAt(LocalDateTime.now());
        product.setIsDeleted(true);

        productRepository.delete(product);
    }
}
