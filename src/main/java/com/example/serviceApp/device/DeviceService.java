package com.example.serviceApp.device;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.device.dto.DeviceDto;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
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
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    public DeviceDto findBySerialNumber(String serialNumber){
       return modelMapper.map(deviceRepository.getDeviceByDeviceSerialNumber(serialNumber)
               .orElseThrow(()-> new IllegalArgumentException("no device found")),DeviceDto.class);
    }
    public DeviceDto addDeviceToCustomer(Long customerId, DeviceDto deviceDto){
        if(deviceRepository.existsByDeviceSerialNumber(deviceDto.getDeviceSerialNumber())){
            throw new IllegalArgumentException("device with serial number "+deviceDto.getDeviceSerialNumber() + " exist");
        }else{
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customerId + " does not exist"));
            List<ServiceRequest> serviceRequestList = new ArrayList<>();
            if(deviceDto.getServiceRequestList()!=null) {
                 serviceRequestList = deviceDto.getServiceRequestList().stream().map((element) -> modelMapper.map(element, ServiceRequest.class)).toList();
            }
            for(ServiceRequest s : serviceRequestList){
                s.getStatusHistory().add(new StatusHistory(ServiceRequest.Status.PENDING,"Request created",s, LocalDateTime.now()));
                s.setStartDate(LocalDate.now());
            }
//            Device device = new Device(deviceDto.getDeviceName(), deviceDto.getDeviceSerialNumber(),serviceRequestList,customer);
            Device device= Device.builder()
                    .deviceSerialNumber(deviceDto.getDeviceSerialNumber())
                    .deviceName(deviceDto.getDeviceName())
                    .serviceRequestList(serviceRequestList)
                    .deviceType(deviceDto.getDeviceType())
                    .customer(customer)
                    .build();
            return modelMapper.map(deviceRepository.save(device), DeviceDto.class);
        }

    }@CacheEvict(value = "services", allEntries = true)
    public DeviceDto addOrUpdateDeviceToCustomer(Long customerId, DeviceDto deviceDto){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customerId + " does not exist"));
        User u = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());
        Optional<Device> optionalDevice = deviceRepository.findByDeviceSerialNumber(deviceDto.getDeviceSerialNumber());
        Device device;
                 if(optionalDevice.isPresent()){
                     device= optionalDevice.get();
                 }else {
                     device= Device.builder()
                             .deviceSerialNumber(deviceDto.getDeviceSerialNumber())
                             .deviceName(deviceDto.getDeviceName())
                             .serviceRequestList(new ArrayList<>())
                             .deviceType(deviceDto.getDeviceType())
                             .build();
                 }

        device.setCustomer(customer);

        if(deviceDto.getServiceRequestList() != null) {
            List<ServiceRequest> serviceRequestList = deviceDto.getServiceRequestList().stream()
                    .map((element) -> modelMapper.map(element, ServiceRequest.class))
                    .collect(Collectors.toList());

            for(ServiceRequest s : serviceRequestList){
                s.setStatusHistory(new ArrayList<>());
                s.getStatusHistory().add(new StatusHistory(ServiceRequest.Status.PENDING,"Request created",s, LocalDateTime.now()));
                s.setLastStatus(ServiceRequest.Status.PENDING);
                s.setStartDate(LocalDate.now());
                s.setDevice(device);
                s.setUser(u);
                device.getServiceRequestList().add(s);
            }
        }

        return modelMapper.map(deviceRepository.save(device), DeviceDto.class);
    }@CacheEvict(value = "services", allEntries = true)
    public void deleteById(Long id) {
        boolean exist = deviceRepository.existsById(id);
        if (exist) {
            try {
                deviceRepository.deleteById(id);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete device", e);
            }
        } else {
            throw new IllegalArgumentException("Device doesn't exist");
        }
    }
}
