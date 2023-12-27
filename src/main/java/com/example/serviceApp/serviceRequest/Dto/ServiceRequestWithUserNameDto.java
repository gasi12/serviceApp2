package com.example.serviceApp.serviceRequest.Dto;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequestWithUserNameDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String description;
    private ServiceRequest.Status lastStatus;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate endDate;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhoneNumber;
    private Long price;
    private String deviceName;
}