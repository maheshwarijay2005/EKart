package com.jaykabra.jaykart.catalog_service.repository;

import com.jaykabra.jaykart.catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  }