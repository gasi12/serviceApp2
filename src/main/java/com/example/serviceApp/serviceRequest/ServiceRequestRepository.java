package com.example.serviceApp.serviceRequest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    @EntityGraph(attributePaths = {"customer", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    List<ServiceRequest> findAll();

    @EntityGraph(attributePaths = {"customer", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<ServiceRequest> findAll(Pageable page);
    List<ServiceRequest> findAllByStatus(ServiceRequest.Status status);
    @Query("SELECT AVG(r.endDate-r.startDate) FROM ServiceRequest r")
    Double findAverageServiceDuration();
    @Query("SELECT new com.example.serviceApp.serviceRequest.RevenuePerPeriod(" +
            "YEAR(s.endDate), MONTH(s.endDate), SUM(s.price)) " +
            "FROM ServiceRequest s " +
            "WHERE YEAR(s.endDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY YEAR(s.endDate), MONTH(s.endDate)")
    List<RevenuePerPeriod> findTotalRevenueGroupedByPeriod();

    @Query("SELECT s.status, count(s.status)from ServiceRequest s where s.endDate is null group by s.status")
    List<Object[]> getStatusNumbers();

}

