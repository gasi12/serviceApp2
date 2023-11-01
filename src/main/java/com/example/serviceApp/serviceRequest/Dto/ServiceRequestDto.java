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
public class ServiceRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String description;
    private ServiceRequest.Status status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate endDate;
    private Long price;
    private Long userId;
}
