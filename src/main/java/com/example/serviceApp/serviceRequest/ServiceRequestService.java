package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithCustomerEditorDto;
import com.example.serviceApp.customExeptions.BadStatusException;
import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithDetailsDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithUserNameDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public List<ServiceRequestDto> getServiceRequests() {

        return serviceRequestRepository.findAll().stream().map((s) -> modelMapper.map(s, ServiceRequestDto.class)).toList();
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
        ServiceRequest newService = modelMapper.map(requestDto, ServiceRequest.class);
        User u = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new IllegalArgumentException());

        Customer newServiceUser = customerRepository.getCustomerById(id).orElseThrow(() ->
                new IllegalArgumentException("user doesnt exist"));
        newService.setCustomer(newServiceUser);
        newService.setUser(u);
        return serviceRequestRepository.save(newService);


    }

    public ServiceRequest findById(Long id) {
        return serviceRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("service with id " + id + " not found"));
    }


    public List<ServiceRequestWithDetailsDto> findAllServiceRequestsWithUserName2(int pageNo, int pageSize) {
        if (pageNo < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or size");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<ServiceRequest> page = serviceRequestRepository.findAll(pageable);

        List<ServiceRequest> serviceRequests = page.getContent();
        return serviceRequests.stream()
                .map(s -> modelMapper.map(s, ServiceRequestWithDetailsDto.class))
                .collect(Collectors.toList());
    }


    public List<ServiceRequestWithDetailsDto> findAllByStatus(String status) {
        if (EnumUtils.isValidEnum(ServiceRequest.Status.class, status)) {

            return serviceRequestRepository.findAllByStatus(ServiceRequest.Status.valueOf(status)).stream().map(serviceRequest -> modelMapper.map(serviceRequest, ServiceRequestWithDetailsDto.class)).toList();
        } else
            throw new BadStatusException("Status not valid");
    }

    @Transactional
    public ServiceRequestDto updateServiceRequest(Long id, ServiceRequestWithCustomerEditorDto serviceRequestDto) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request with id " + id + " not found"));
        log.info(serviceRequest.toString());
        modelMapper.map(serviceRequestDto, serviceRequest);//todo mapper mapuje id i  sie robi afera
       // serviceRequest.setDescription(serviceRequestDto.getDescription());
       // serviceRequest.setPrice(serviceRequestDto.getPrice());
       // serviceRequest.setStatus(serviceRequestDto.getStatus());
        serviceRequestRepository.save(serviceRequest);
        return modelMapper.map(serviceRequest, ServiceRequestDto.class);
    }


    @Transactional
    public ServiceRequest updateServiceRequestWithUser(Long id, ServiceRequestWithUserNameDto request) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service request with id" + id + " not found"));
        Optional<Customer> user = customerRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user.isPresent()) {
            if (!serviceRequest.getCustomer().getPhoneNumber().equals(user.get().getPhoneNumber())) {
                throw new IllegalArgumentException("Mismatched phone numbers: service request user and provided user");
            }
        }
        Customer customer = serviceRequest.getCustomer();
//        modelMapper.map(request, customer);
//        modelMapper.map(request, serviceRequest);//todo nie dziala
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhoneNumber(request.getPhoneNumber());
        serviceRequest.setPrice(request.getPrice());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setStatus(request.getStatus());
        if (serviceRequest.getStatus().equals(ServiceRequest.Status.FINISHED)) {
            serviceRequest.setEndDate(LocalDate.now());
        }
        return serviceRequestRepository.save(serviceRequest);
    }
    //todo przeniesc to do analityki czy cos

    public Double findAverageServiceDuration() {
        return serviceRequestRepository.findAverageServiceDuration();

    }

    public List<RevenuePerPeriod> getRevenueByPeriod() {
        return serviceRequestRepository.findTotalRevenueGroupedByPeriod();
    }

    public Map<ServiceRequest.Status, Long> countStatus() {
        List<Object[]> query = serviceRequestRepository.getStatusNumbers();
        Map<ServiceRequest.Status, Long> statusCounts = new HashMap<>();
        for (Object[] result : query) {
            statusCounts.put((ServiceRequest.Status) result[0], (Long) result[1]);
        }
        return statusCounts;
    }
}
