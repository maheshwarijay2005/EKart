package com.jaykabra.jaykart.repository;

import com.jaykabra.jaykart.entity.Customer;
import com.jaykabra.jaykart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByEmail(String email);
  Optional<Customer> findByEmailOrMobileNumber(String email, String mobileNumber);
}