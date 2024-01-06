package com.example.serviceApp.serviceRequest;


import com.example.serviceApp.serviceRequest.Dto.RevenuePerPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    @EntityGraph(attributePaths = {"user","statusHistory"}, type = EntityGraph.EntityGraphType.LOAD)
    List<ServiceRequest> findAll();

    @EntityGraph(attributePaths = {"customer", "user","statusHistory"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<ServiceRequest> findAll(Pageable pageable);
    @EntityGraph(value = "ServiceRequest.deviceAndCustomer")
    List<ServiceRequest> findAllByLastStatus(ServiceRequest.Status status);

    @EntityGraph(attributePaths = {"device", "statusHistory", "device.customer"})
    Optional<ServiceRequest> findWithDetailsById(Long id);

    @EntityGraph(value = "ServiceRequest.deviceAndCustomer")
    List<ServiceRequest> findByIdNotNullOrderByIdDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"device", "statusHistory", "device.customer"})
    List<ServiceRequest> findAllByLastStatusNotOrderByIdDesc(ServiceRequest.Status status,Pageable pageable);
    @EntityGraph(attributePaths = {"device", "statusHistory", "device.customer"})
    List<ServiceRequest> findAllByLastStatusOrderByIdDesc(ServiceRequest.Status status,Pageable pageable);

    @Query("SELECT AVG(r.endDate-r.startDate) FROM ServiceRequest r")
    Double findAverageServiceDuration();
    @Query("SELECT new com.example.serviceApp.serviceRequest.Dto.RevenuePerPeriod(" +
            "YEAR(s.endDate), MONTH(s.endDate), SUM(s.price)) " +
            "FROM ServiceRequest s " +
            "WHERE YEAR(s.endDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY YEAR(s.endDate), MONTH(s.endDate)")
    List<RevenuePerPeriod> findTotalRevenueGroupedByPeriod();

    @Query("SELECT s.lastStatus, count(s.lastStatus)from ServiceRequest s where s.endDate is null group by s.lastStatus")
    List<Object[]> getStatusNumbers();

}

