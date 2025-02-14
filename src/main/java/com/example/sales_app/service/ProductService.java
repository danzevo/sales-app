package com.example.sales_app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.sales_app.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import com.example.sales_app.repository.ProductRepository;
import com.example.sales_app.request.ProductRequest;
import com.example.sales_app.util.SecurityUtil;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        try{
            return productRepository.findAllNonDeleted();
        } catch (Exception e) {
            logger.error("Error retrieving all products", e);
            throw new RuntimeException("Unable to retrieve products", e);
        }
    }

    public void addProduct(ProductRequest request) {
        try {
            Product product = new Product();
            product.setName(request.getName());
            product.setStock(request.getStock());
            product.setPrice(request.getPrice());
            product.setCreatedBy(SecurityUtil.getCurrentUser());
            product.setCreatedAt(LocalDateTime.now());
            productRepository.save(product);
        } catch(Exception e) {
            logger.error("Error retrieving all products", request.getName(), e);
            throw new RuntimeException("unable to retrieve products", e);
        }
    }

    public void updateProduct(Long id, ProductRequest request) {
        try{
            Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
            product.setName(request.getName());
            product.setStock(request.getStock());
            product.setPrice(request.getPrice());
            product.setChangedBy(SecurityUtil.getCurrentUser());
            product.setChangedAt(LocalDateTime.now());
            productRepository.save(product);
        } catch (Exception e) {
            logger.error("Error updating product with ID {}", id, e);
            throw new RuntimeException("Unable to update product", e);
        }
    }

    public void deleteProduct(Long id) {
        try{
            Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));

            product.setDeletedBy(SecurityUtil.getCurrentUser());
            product.setDeletedAt(LocalDateTime.now());
            product.setIsDeleted(true);

            productRepository.delete(product);
        } catch (Exception e) {
            logger.error("Error deleting product with ID {}", id, e);
            throw new RuntimeException("Unable to delete product", e);
        }
    }
}
