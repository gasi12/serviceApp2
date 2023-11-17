package com.example.serviceApp.customer.Dto;

import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.example.serviceApp.customer.Customer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDtoWithTempPassword implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String plainPassword;
    private Role role;
    private Long phoneNumber;
    private List<ServiceRequestDto> serviceRequestList;

    /**
     * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceRequestDto implements Serializable {
        private Long id;
        private String description;
        private ServiceRequest.Status status;
        private LocalDate endDate;
        private LocalDate startDate;
        private Long price;
        private Long userId;
    }
}