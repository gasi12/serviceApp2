package com.example.serviceApp.serviceRequest.status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory,Long> {

}
