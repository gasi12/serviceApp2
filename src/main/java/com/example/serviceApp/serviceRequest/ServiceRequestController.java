package com.example.serviceApp.serviceRequest;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @GetMapping("/service/{id}")
    public ServiceRequestDto findById (@PathVariable Long id){
        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.findById(id));
    }

    @GetMapping("/services")
    List<ServiceRequest> getAllRequests(){
        return serviceRequestService.getServiceRequests();
    }
    @PutMapping("/service/addtouser/{id}")
    public  ServiceRequestDto addServiceToUser(@PathVariable Long id,@RequestBody ServiceRequestDto requestDto){
         return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.addServiceToUser(id,requestDto));
    }
    @DeleteMapping("/service/{id}")
    public void deleteServiceRequest(@PathVariable Long id){
        serviceRequestService.deleteById(id);
    }
    @GetMapping("/service-requests-with-user-name")
    public List<ServiceRequestWithUserNameDto> getAllServiceRequestsWithUserName() {
        return serviceRequestService.findAllServiceRequestsWithUserName();
    }
    @GetMapping("/service/status/{status}")
    public List<ServiceRequest> findAllByStatus(@PathVariable ServiceRequest.Status status){
        return serviceRequestService.findAllByStatus(status);
    }
    @PutMapping("/service/{id}")
    public ServiceRequest updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequestDto serviceRequestDto) {
        // Update the record using the serviceRequestService or repository
        return serviceRequestService.updateServiceRequest(id, serviceRequestDto);
    }
@PutMapping("/service/{id}/user")
    public ServiceRequest updateServiceRequestWithUser(@PathVariable Long id,@RequestBody ServiceRequestWithUserNameDto service) {
 return serviceRequestService.updateServiceRequestWithUser(id,service);
    }
}
