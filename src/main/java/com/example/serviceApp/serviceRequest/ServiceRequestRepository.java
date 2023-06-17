package com.example.serviceApp.serviceRequest;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    @EntityGraph(attributePaths = {"appUser"},type = EntityGraph.EntityGraphType.LOAD)
    List<ServiceRequest> findAll();
    @EntityGraph(attributePaths = {"appUser"},type = EntityGraph.EntityGraphType.LOAD)
    List<ServiceRequest> findAll(Sort sort);
    List<ServiceRequest> findAllByStatus(ServiceRequest.Status status);
}

