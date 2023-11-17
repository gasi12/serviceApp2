package com.example.serviceApp.serviceRequest.Dto;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestWithDetailsDto implements Serializable {
    private Long id;
    private String description;
    private ServiceRequest.Status status;
    private LocalDate endDate;
    private LocalDate startDate;
    private Long price;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhoneNumber;
    private Long userId;
}