package com.example.serviceApp.customer.Dto;

import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWithRequestsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private List<ServiceRequestDto> serviceRequestList;

}
