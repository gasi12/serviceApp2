package com.example.serviceApp.serviceRequest.newDtos;

import com.example.serviceApp.device.Device;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ServiceRequest}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestSummaryDto implements Serializable {
    private Long id;
    private String description;
    private ServiceRequest.Status lastStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long price;
    private String deviceName;
    private Device.deviceType deviceType;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhoneNumber;
    private Long userId;
}