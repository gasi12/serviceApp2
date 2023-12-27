package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.serviceRequest.Dto.*;
import com.example.serviceApp.customExeptions.BadStatusException;
import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import com.example.serviceApp.serviceRequest.status.StatusHistoryDto;
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
import java.time.LocalDateTime;
import java.util.*;
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
    public ServiceRequest addServiceRequestToUser(Long id, ServiceRequestWithUserNameDto requestDto) {
        ServiceRequest newService = modelMapper.map(requestDto, ServiceRequest.class);
        List<StatusHistory> status = new ArrayList<>();
        status.add(new StatusHistory(ServiceRequest.Status.PENDING, "Request created", newService, LocalDateTime.now()));

        User u = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new IllegalArgumentException("user not found"));

        Customer newServiceUser = customerRepository.getCustomerById(id).orElseThrow(() ->
                new IllegalArgumentException("customer of given id doesnt exist"));
        newServiceUser.setFirstName(requestDto.getCustomerFirstName());
        newServiceUser.setLastName(requestDto.getCustomerLastName());
        newServiceUser.setPhoneNumber(requestDto.getCustomerPhoneNumber());

        newService.setStatusHistory(status);
        newService.setCustomer(newServiceUser);
        newService.setLastStatus(ServiceRequest.Status.PENDING);
        newService.setDeviceName(requestDto.getDeviceName());
        newService.setUser(u);

        return serviceRequestRepository.save(newService);
    }

    public ServiceRequestWithDetailsDto findById(Long id) {
        return modelMapper.map(serviceRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("service with id " + id + " not found")), ServiceRequestWithDetailsDto.class);
    }


    public List<ServiceRequestWithUserNameDto> findAllServiceRequestsWithUserName2(int pageNo, int pageSize) {
        if (pageNo < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or size");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<ServiceRequest> page = serviceRequestRepository.findAll(pageable);

        List<ServiceRequest> serviceRequests = page.getContent();
        //na szczescie pisze to sam wiec nikt tego prawdopodbnie nie zobaczy
        return serviceRequests.stream().map(s -> {


            return ServiceRequestWithUserNameDto.builder()
                    .id(s.getId())
                    .description(s.getDescription())
                    .endDate(s.getEndDate())
                    .startDate(s.getStartDate())
                    .price(s.getPrice())
                    .customerFirstName(s.getCustomer().getFirstName())
                    .customerLastName(s.getCustomer().getLastName())
                    .customerPhoneNumber(s.getCustomer().getPhoneNumber())
                    .lastStatus(s.getLastStatus())
                    .deviceName(s.getDeviceName())
                    .build();
        }).collect(Collectors.toList());

// nie mowie, jest to schludne aczkolwiek pomija za duzo pol,
// a ja nie mam zamiaru konfigurowac mappera dla jednej funkcji
// nie ma teraz na to czasu
//        return serviceRequests.stream()
//                .map(s -> modelMapper.map(s, ServiceRequestWithDetailsDto.class))
//                .collect(Collectors.toList());
    }


    public List<ServiceRequestWithUserNameDto> findAllByStatus(String status) {
        if (EnumUtils.isValidEnum(ServiceRequest.Status.class, status)) {

            return serviceRequestRepository.findAllByLastStatus(ServiceRequest.Status.valueOf(status)).stream().map(serviceRequest -> modelMapper.map(serviceRequest, ServiceRequestWithUserNameDto.class)).toList();
        } else
            throw new BadStatusException("Status not valid");
    }

    @Transactional
    public ServiceRequestWithDetailsDto updateServiceRequest(Long id, ServiceRequestUpdateDto serviceRequestDto) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request with id " + id + " not found"));
        log.info(serviceRequest.toString());
        modelMapper.map(serviceRequestDto, serviceRequest);//todo mapper mapuje id i  sie robi afera
        // serviceRequest.setDescription(serviceRequestDto.getDescription());
        // serviceRequest.setPrice(serviceRequestDto.getPrice());
        // serviceRequest.setStatus(serviceRequestDto.getStatus());
        serviceRequestRepository.save(serviceRequest);
        return modelMapper.map(serviceRequest, ServiceRequestWithDetailsDto.class);
    }


    @Transactional
    public ServiceRequestWithUserNameDto updateServiceRequestWithUser(Long id, ServiceRequestWithUserNameDto request) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service request with id" + id + " not found"));
        Optional<Customer> customerFromDb = customerRepository.findByPhoneNumber(request.getCustomerPhoneNumber());
        if (customerFromDb.isPresent() && !customerFromDb.get().getId().equals(serviceRequest.getCustomerId())) {
            throw new IllegalArgumentException("user with given number already exist");
        }
        Customer customer = serviceRequest.getCustomer();
        customer.setFirstName(request.getCustomerFirstName());
        customer.setLastName(request.getCustomerLastName());
        customer.setPhoneNumber(request.getCustomerPhoneNumber());
        serviceRequest.setPrice(request.getPrice());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setDeviceName(request.getDeviceName());
        serviceRequest.setCustomer(customer);

        return modelMapper.map(serviceRequestRepository.save(serviceRequest), ServiceRequestWithUserNameDto.class);
    }

    public StatusHistoryDto addStatus(Long serviceId, StatusHistoryDto status) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceId).orElseThrow(() -> new IllegalArgumentException("Service " + serviceId + " not found"));
        List<StatusHistory> existingStatusList = serviceRequest.getStatusHistory();
        serviceRequest.setLastStatus(status.getStatus());
        StatusHistory statusToAdd = new StatusHistory(status.getStatus(), status.getComment(), serviceRequest, LocalDateTime.now());
        if (status.getStatus().equals(ServiceRequest.Status.FINISHED)) {
            serviceRequest.setEndDate(LocalDate.now());
        }
        existingStatusList.add(statusToAdd);
        serviceRequest.setStatusHistory(existingStatusList);
        serviceRequestRepository.save(serviceRequest);
        return modelMapper.map(serviceRequest.getStatusHistory().get(serviceRequest.getStatusHistory().size() - 1), StatusHistoryDto.class);
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
