package com.example.serviceApp.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    void getUserById(Long id);

    boolean existsByPhoneNumber(Long phoneNumber);
    Optional<Customer> getCustomerByPhoneNumber(Long phoneNumber);
    Optional<Customer> getCustomerById(Long phoneNumber);

    Optional<Customer> findByPhoneNumber(Long phoneNumber);
}

