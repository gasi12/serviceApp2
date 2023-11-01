package com.example.serviceApp.serviceRequest.Dto;

import com.example.serviceApp.serviceRequest.ServiceRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceRequestDtoMapper {
    public static List<ServiceRequestDto> mapToServiceRequestsDtos(List<ServiceRequest> requests) {
        return requests.stream().map(request -> mapToServiceRequestDto(request)).
                collect(Collectors.toList());
    }

    public static ServiceRequestDto mapToServiceRequestDto( ServiceRequest request) {
        return ServiceRequestDto.builder()
                .description(request.getDescription())
                .status(request.getStatus())
                .startDate(request.getStartDate())
                .id(request.getId())
                .price(request.getPrice())
                .endDate(request.getEndDate())
                .userId(request.getUser().getId())
                .build();
    }
}
