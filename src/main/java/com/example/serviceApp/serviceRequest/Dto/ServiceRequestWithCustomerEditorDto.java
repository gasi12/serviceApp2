package com.example.serviceApp.serviceRequest.Dto;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestWithCustomerEditorDto implements Serializable {

    private String description;
    private ServiceRequest.Status status = ServiceRequest.Status.PENDING;
    private Long price;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhoneNumber;
}