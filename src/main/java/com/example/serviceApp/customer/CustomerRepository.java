package com.example.serviceApp.customer;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> getCustomerByPhoneNumber(Long phoneNumber);






        Optional<Customer> findByPhoneNumber(Long phoneNumber);



    @EntityGraph(attributePaths = {"devices"})
    Optional<Customer> getCustomerByIdOrderById(Long id);
    @EntityGraph(attributePaths = {"devices"})
    Optional<Customer> getCustomerWithDevicesByPhoneNumber(Long phoneNumber);


    Optional<Customer> getCustomerById(Long id);
}

