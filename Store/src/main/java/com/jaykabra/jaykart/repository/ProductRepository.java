package com.jaykabra.jaykart.repository;

import com.jaykabra.jaykart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  }