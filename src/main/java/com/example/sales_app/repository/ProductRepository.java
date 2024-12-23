package com.example.sales_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sales_app.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("Select p From Product p where p.isDeleted = false")
    List<Product> findAllNonDeleted();
}   
