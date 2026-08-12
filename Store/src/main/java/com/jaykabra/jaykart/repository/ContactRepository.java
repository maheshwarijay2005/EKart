package com.jaykabra.jaykart.repository;

import com.jaykabra.jaykart.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByStatus(String status);

    @Query(name = "Contact.findByStatus")
    List<Contact> fetchByStatus(String status);

    List<Contact> findByStatusWithNativeQuery(String status);

  }