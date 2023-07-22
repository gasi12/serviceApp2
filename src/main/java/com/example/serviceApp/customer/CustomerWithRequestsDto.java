package com.example.serviceApp.customer;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWithRequestsDto {
    private Long id;
    private String customerName;
    private Long phoneNumber;
    private List<ServiceRequest> serviceRequestList;
}
