package com.example.marketspring.repositories;

import com.example.marketspring.models.Product;
import com.example.marketspring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);
}
