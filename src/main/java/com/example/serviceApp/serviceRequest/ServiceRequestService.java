package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.device.Device;
import com.example.serviceApp.device.DeviceRepository;

import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.newDtos.ServiceRequestDtoAll;
import com.example.serviceApp.serviceRequest.newDtos.ServiceRequestSummaryDto;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import com.example.serviceApp.serviceRequest.status.dto.StatusHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceRequestService {
    private final DeviceRepository deviceRepository;

    private final ServiceRequestRepository serviceRequestRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Cacheable(value = "services", key = "#id")
    public ServiceRequestDtoAll findByIdWithAllDetails(Long id){
        ServiceRequest s = serviceRequestRepository.findWithDetailsById(id).orElseThrow(()-> new IllegalArgumentException("service not found"));
        List<ServiceRequestDtoAll.StatusHistoryDtoAll> list = new ArrayList<>();
        for(StatusHistory status :s.getStatusHistory()){
           list.add(ServiceRequestDtoAll.StatusHistoryDtoAll.builder()
                   .comment(status.getComment())
                   .status(status.getStatus())
                   .id(status.getId())
                   .time(status.getTime())
                   .build());
        }
           return ServiceRequestDtoAll.builder()
                    .id(s.getId())
                    .endDate(s.getEndDate())
                    .price(s.getPrice())
                    .lastStatus(s.getLastStatus())
                    .startDate(s.getStartDate())
                   .statusHistoryList(list)
                   // .statusHistoryList(s.getStatusHistory().stream().map((element) -> modelMapper.map(element, ServiceRequestDtoAll.StatusHistoryDtoAll.class)).collect(Collectors.toList()))
                    .description(s.getDescription())
                    .customer(modelMapper.map(s.getDevice().getCustomer(), ServiceRequestDtoAll.DeviceDtoAll.CustomerDtoAll.class))
                    .device(modelMapper.map(s.getDevice(), ServiceRequestDtoAll.DeviceDtoAll.class))
                    .build();
//return modelMapper.map(serviceRequestRepository.findWithDetailsById(id).orElseThrow(()-> new IllegalArgumentException("service not found")), ServiceRequestDtoAll.class);//todo znowu to nie dziala aaaaaaaaaaaaaaaaaaaaaaaa
    }
    @Cacheable(value = "services", key = "#page")
    public List<ServiceRequestSummaryDto> getServiceRequests(Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        List <ServiceRequest>allServices =  serviceRequestRepository.findByIdNotNullOrderByIdDesc(pageable);
        return mapToServiceRequestSummaryDtos(allServices);
    }
    @Cacheable(value = "services", key = "#page")
    public List<ServiceRequestSummaryDto> getServiceRequestsByStatusNotFinished(Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        List <ServiceRequest>allServices =  serviceRequestRepository.findAllByLastStatusNotOrderByIdDesc(ServiceRequest.Status.FINISHED,pageable);
        return mapToServiceRequestSummaryDtos(allServices);
    }


    @CacheEvict(value = "services", allEntries = true)
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
    @Cacheable(value = "services", key = "#page")
    public List<ServiceRequestSummaryDto> findByStatus(Integer page, Integer size, ServiceRequest.Status status) {
        Pageable pageable = PageRequest.of(page,size);
        List <ServiceRequest>allServices =  serviceRequestRepository.findAllByLastStatusOrderByIdDesc(status,pageable);
        return mapToServiceRequestSummaryDtos(allServices);
    }

@CacheEvict(value = "services", allEntries = true)
    public StatusHistoryDto addStatus(Long serviceId, StatusHistoryDto status) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceId).orElseThrow(() -> new IllegalArgumentException("Service " + serviceId + " not found"));
        List<StatusHistory> existingStatusList = serviceRequest.getStatusHistory();
        serviceRequest.setLastStatus(status.getStatus());
        StatusHistory statusToAdd = new StatusHistory(status.getStatus(), status.getComment(), serviceRequest, LocalDateTime.now());
        if (status.getStatus().equals(ServiceRequest.Status.FINISHED)) {
            serviceRequest.setEndDate(LocalDateTime.now());
        }
        existingStatusList.add(statusToAdd);
        serviceRequest.setStatusHistory(existingStatusList);
        serviceRequestRepository.save(serviceRequest);
        return modelMapper.map(serviceRequest.getStatusHistory().get(serviceRequest.getStatusHistory().size() - 1), StatusHistoryDto.class);
    }
    @CacheEvict(value = "services", allEntries = true)
  public   ServiceRequestAddDto addServiceToDevice(Long deviceId,ServiceRequestAddDto requestAddDto){
        Optional<Device> device =deviceRepository.findById(deviceId);
        User u = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());
         if (device.isEmpty()){
             throw new IllegalArgumentException("no device with id " + deviceId);
         }else {

             Device d = device.get();
             ServiceRequest newServiceRequest = ServiceRequest.builder()
                     .description(requestAddDto.getDescription())
                     .price(requestAddDto.getPrice())
                     .lastStatus(ServiceRequest.Status.PENDING)
                     .device(device.get())
                     .user(u)
                     .build();
             List<StatusHistory> list = new ArrayList<>();
             StatusHistory status = new StatusHistory(ServiceRequest.Status.PENDING, "Request created", newServiceRequest, LocalDateTime.now());
             list.add(status);
             newServiceRequest.setStatusHistory(list);
             d.getServiceRequestList().add(newServiceRequest);
            return modelMapper.map(serviceRequestRepository.save(newServiceRequest), ServiceRequestAddDto.class);
         }
    }
    @CacheEvict(value = "services", allEntries = true)
    public   ServiceRequestAddDto editServiceRequest(Long id, ServiceRequestAddDto requestAddDto){

        ServiceRequest editedRequest = serviceRequestRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("status of id "+ id + " not found"));
        editedRequest.setDescription(requestAddDto.getDescription());
        editedRequest.setPrice(requestAddDto.getPrice());
        return modelMapper.map(serviceRequestRepository.save(editedRequest), ServiceRequestAddDto.class);
    }
    private List<ServiceRequestSummaryDto> mapToServiceRequestSummaryDtos(List<ServiceRequest> allServices) {
        return allServices.stream()
                .map(s-> ServiceRequestSummaryDto
                        .builder()
                        .id(s.getId())
                        .description(s.getDescription())
                        .lastStatus(s.getLastStatus())
                        .price(s.getPrice())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .deviceName(s.getDevice().getDeviceName())
                        .deviceType(s.getDevice().getDeviceType())
                        .customerFirstName(s.getDevice().getCustomer().getFirstName())
                        .customerLastName(s.getDevice().getCustomer().getFirstName())
                        .customerPhoneNumber(s.getDevice().getCustomer().getPhoneNumber())
                        .customerId(s.getDevice().getCustomer().getId())
                        .userId(s.getUser().getId())

                        .build())
                .collect(Collectors.toList());
    }
//
//    //todo przeniesc to do analityki czy cos
//
//    public Double findAverageServiceDuration() {
//        return serviceRequestRepository.findAverageServiceDuration();
//
//    }
//
//    public List<RevenuePerPeriod> getRevenueByPeriod() {
//        return serviceRequestRepository.findTotalRevenueGroupedByPeriod();
//    }
//
//    public Map<ServiceRequest.Status, Long> countStatus() {
//        List<Object[]> query = serviceRequestRepository.getStatusNumbers();
//        Map<ServiceRequest.Status, Long> statusCounts = new HashMap<>();
//        for (Object[] result : query) {
//            statusCounts.put((ServiceRequest.Status) result[0], (Long) result[1]);
//        }
//        return statusCounts;
//    }
}
