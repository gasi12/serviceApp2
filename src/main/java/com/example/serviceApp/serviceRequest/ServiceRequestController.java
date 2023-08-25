package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDtoMapper;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithUserNameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @GetMapping("/service/{id}")
    public ServiceRequestDto findById(@PathVariable Long id) {
        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.findById(id));
    }

    @GetMapping("/services")
    List<ServiceRequest> getAllRequests() {
        return serviceRequestService.getServiceRequests();
    }

    @PutMapping("/service/addtouser/{id}")
    public ServiceRequestDto addServiceToUser(@PathVariable Long id, @RequestBody ServiceRequestDto requestDto) {
        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.addServiceToUser(id, requestDto));
    }

    @DeleteMapping("/service/{id}")
    public void deleteServiceRequest(@PathVariable Long id) {
        serviceRequestService.deleteById(id);
    }

    @GetMapping("/service-requests-with-user-name")
    public List<ServiceRequestWithUserNameDto> getAllServiceRequestsWithUserName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return serviceRequestService.findAllServiceRequestsWithUserName(pageNo, pageSize);
    }

    @GetMapping("/service/status/{status}")
    public List<ServiceRequest> findAllByStatus(@PathVariable ServiceRequest.Status status) {
        return serviceRequestService.findAllByStatus(status);
    }

    @PutMapping("/service/{id}")
    public ServiceRequest updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequestDto serviceRequestDto) {
        return serviceRequestService.updateServiceRequest(id, serviceRequestDto);
    }

    @PutMapping("/service/{id}/user")
    public ServiceRequest updateServiceRequestWithUser(@PathVariable Long id, @RequestBody ServiceRequestWithUserNameDto service) {
        return serviceRequestService.updateServiceRequestWithUser(id, service);
    }
}
