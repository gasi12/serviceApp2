package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithUserNameDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;


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
    public ServiceRequest addServiceRequestToUser(Long id, ServiceRequestDto requestDto) {
        ServiceRequest newService = modelMapper.map(requestDto,ServiceRequest.class);

        Customer newServiceUser = customerRepository.getCustomerById(id).orElseThrow(() ->
                new IllegalArgumentException("user doesnt exist"));
        newService.setCustomer(newServiceUser);
        return serviceRequestRepository.save(newService);
    }

    public ServiceRequest findById(Long id) {
        return serviceRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("service with id "+id +" not found"));
    }


public List<ServiceRequestWithUserNameDto> findAllServiceRequestsWithUserName(int pageNo, int pageSize) {
    if (pageNo < 0 || pageSize <= 0) {
        throw new IllegalArgumentException("Invalid page number or size");
    }

    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
    Page<ServiceRequest> page = serviceRequestRepository.findAll(pageable);

    List<ServiceRequest> serviceRequests = page.getContent();
    return serviceRequests.stream()
            .map(s -> modelMapper.map(s, ServiceRequestWithUserNameDto.class))
            .collect(Collectors.toList());
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
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request with id "+id+" not found"));

        modelMapper.map(serviceRequestDto, serviceRequest);

        return serviceRequestRepository.save(serviceRequest);
    }



    @Transactional
    public ServiceRequest updateServiceRequestWithUser(Long id, ServiceRequestWithUserNameDto request) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service request with id" +id+ " not found"));
        Optional<Customer> user = customerRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user.isPresent()) {
            if (!serviceRequest.getCustomer().getPhoneNumber().equals(user.get().getPhoneNumber())) {
                throw new IllegalArgumentException("Mismatched phone numbers: service request user and provided user");
            }
        }
        Customer customer = serviceRequest.getCustomer();
        modelMapper.map(request, customer);
        modelMapper.map(request, serviceRequest);
        if (serviceRequest.getStatus().equals(ServiceRequest.Status.FINISHED)) {
            serviceRequest.setEndDate(LocalDate.now());
        }
        return serviceRequestRepository.save(serviceRequest);
    }
}
