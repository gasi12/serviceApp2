package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDtoMapper;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithDetailsDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithUserNameDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;
    private final ConcurrentHashMap<Long, List<String>> userOrdersCache = new ConcurrentHashMap<>();

    @GetMapping("/cache")
    public String getUserOrders() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userOrdersCache);
    }

    @GetMapping("/service/{id}")//ok
    public ServiceRequestDto findById(@PathVariable Long id) {
        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.findById(id));
    }

    @GetMapping("/services")//ok
    List<ServiceRequestDto> getAllRequests() {
        return serviceRequestService.getServiceRequests();
    }

    @PutMapping("/service/addtouser/{id}")//ok
    public ServiceRequestDto addServiceToUser(@PathVariable Long id, @RequestBody ServiceRequestDto requestDto) {
        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.addServiceRequestToUser(id, requestDto));
    }

    @DeleteMapping("/service/{id}")//ok
    public void deleteServiceRequest(@PathVariable Long id) {
        serviceRequestService.deleteById(id);
    }

    @GetMapping("/service-requests-with-user-name")//ok
    public List<ServiceRequestWithDetailsDto> getAllServiceRequestsWithUserName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return serviceRequestService.findAllServiceRequestsWithUserName2(pageNo, pageSize);
    }

    @GetMapping("/service/status/{status}")//ok
    public List<ServiceRequestWithDetailsDto> findAllByStatus(@PathVariable String status) {
        return serviceRequestService.findAllByStatus(status);
    }

    @PutMapping("/service/{id}")//ok
    public ServiceRequestDto updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequestDto serviceRequestDto) {
        return serviceRequestService.updateServiceRequest(id, serviceRequestDto);
    }

    @PutMapping("/service/{id}/user")//ta metoda nie dziala, ale tez jest bez sensu imo
    public ServiceRequest updateServiceRequestWithUser(@PathVariable Long id, @RequestBody ServiceRequestWithUserNameDto service) {
        return serviceRequestService.updateServiceRequestWithUser(id, service);
    }
}
