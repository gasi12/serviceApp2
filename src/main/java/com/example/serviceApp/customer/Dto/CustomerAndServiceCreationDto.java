package com.example.serviceApp.customer.Dto;

import com.example.serviceApp.customer.Customer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Customer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAndServiceCreationDto implements Serializable {
    private String firstName;
    private String lastName;
    private Long phoneNumber;

    private List<ServiceRequestCreationDto> serviceRequestList;

    /**
     * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceRequestCreationDto implements Serializable {
        private String description;
        private Long price;
        private String deviceName;
    }
}