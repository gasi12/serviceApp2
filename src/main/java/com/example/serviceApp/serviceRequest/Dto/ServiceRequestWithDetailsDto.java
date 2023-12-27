package com.example.serviceApp.serviceRequest.Dto;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestWithDetailsDto implements Serializable {
    private Long id;
    private String description;
    private LocalDate endDate;
    private LocalDate startDate;
    private Long price;
//    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhoneNumber;
    private Long userId;
    private ServiceRequest.Status lastStatus;
    private List<StatusHistoryDto> statusHistory;

    /**
     * DTO for {@link com.example.serviceApp.serviceRequest.status.StatusHistory}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatusHistoryDto implements Serializable {
        private Long id;
        private ServiceRequest.Status status;
        private String comment;
        private LocalDateTime time;
    }
}