package com.example.serviceApp.serviceRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestWithUserNameDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String description;
    private ServiceRequest.Status status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate endDate;
    private String userName;
    private Long phoneNumber;
    private Long price;
}