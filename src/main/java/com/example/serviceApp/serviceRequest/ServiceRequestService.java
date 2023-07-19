package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Service
@RequiredArgsConstructor

public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    public List<ServiceRequest> getServiceRequests() {
        return serviceRequestRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        boolean exist = serviceRequestRepository.existsById(id);
        if (exist) {
            try {
                serviceRequestRepository.deleteById(id);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete service request", e);
            }
        } else {
            throw new IllegalArgumentException("Service doesn't exist");
        }
    }
    @Transactional
    public ServiceRequest addServiceToUser(Long id, ServiceRequestDto requestDto) {
        ServiceRequest newService = new ServiceRequest();
        newService.setDescription(requestDto.getDescription());
        Customer newServiceUser = customerRepository.getCustomerById(id).orElseThrow(() ->
                new IllegalArgumentException("user doenst exist"));
        newService.setCustomer(newServiceUser);
        return serviceRequestRepository.save(newService);
    }

    public ServiceRequest findById(Long id) {
        return serviceRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
    }


public List<ServiceRequestWithUserNameDto> findAllServiceRequestsWithUserName(int pageNo, int pageSize) {
    List<ServiceRequestWithUserNameDto> serviceRequestDtos = new ArrayList<>();
    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
    Page<ServiceRequest> page = serviceRequestRepository.findAll(pageable);
    List<ServiceRequest> serviceRequests = page.getContent();
    for (ServiceRequest s : serviceRequests) {
        serviceRequestDtos.add(modelMapper.map(s, ServiceRequestWithUserNameDto.class));
    }
    return serviceRequestDtos;
}



    public List<ServiceRequest> findAllByStatus(ServiceRequest.Status status) {
        for (ServiceRequest.Status s : ServiceRequest.Status.values()) {
            if (s.name().equals(status.name())) {
                return serviceRequestRepository.findAllByStatus(status);
            }
        }
        throw new IllegalArgumentException("Status not valid");


    }

    @Transactional
    public ServiceRequest updateServiceRequest(Long id, ServiceRequestDto serviceRequestDto) {
        try {
            ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Service request not found"));

            objectMapper.updateValue(serviceRequest, serviceRequestDto);

            return serviceRequestRepository.save(serviceRequest);
        } catch (JsonMappingException e) {
            // Handle the exception
            // For example, you can log the error or throw a custom exception
            throw new RuntimeException("Error updating service request", e);
        }
    }


    @Transactional
    public ServiceRequest updateServiceRequestWithUser(Long id, ServiceRequestWithUserNameDto request) {//todo zmapowac tego potwora ifowego

        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));
        Optional<Customer> user = customerRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user.isPresent()) {
            if (!serviceRequest.getCustomer().getPhoneNumber().equals(user.get().getPhoneNumber())) {
                throw new IllegalArgumentException("Mismatched phone numbers: service request user and provided user");
            }
        }
        Customer customer = serviceRequest.getCustomer();
        customer.setCustomerName(request.getCustomerName());
        customer.setPhoneNumber(request.getPhoneNumber());
        serviceRequest.setPrice(request.getPrice());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setStatus(request.getStatus());
        if (serviceRequest.getStatus().equals(ServiceRequest.Status.FINISHED)) {
            serviceRequest.setEndDate(LocalDate.now());
        }
        return serviceRequestRepository.save(serviceRequest);
    }
}
