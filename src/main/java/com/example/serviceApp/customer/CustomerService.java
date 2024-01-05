package com.example.serviceApp.customer;

import com.example.serviceApp.EmailServiceImpl;
import com.example.serviceApp.customer.Dto.devicedto.*;
import com.example.serviceApp.device.Device;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.ServiceRequestAddDto;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import jakarta.transaction.Transactional;
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

@Service
@Slf4j
    @RequiredArgsConstructor
    public class CustomerService {

        private final CustomerRepository customerRepository;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;
        private final EmailServiceImpl emailService;

        public void sendEmail(){
            emailService.sendEmail("gussy1258@gmail.com","TO JEST MAIL Z APKI","Brawo");
        }

        public CustomerAndDevicesDto findCustomerById(Long id){
            log.info("before");
Customer c = customerRepository.getCustomerByIdOrderById(id).orElseThrow();
            List<CustomerAndDevicesDto.DeviceDto> deviceDtos = c.getDevices().stream()
                    .map(device -> modelMapper.map(device, CustomerAndDevicesDto.DeviceDto.class))
                    .toList();

return CustomerAndDevicesDto.builder()
        .id(c.getId())
        .firstName(c.getFirstName())
        .lastName(c.getLastName())
        .phoneNumber(c.getPhoneNumber())
        .devices(deviceDtos)
        .build();


    }
    public CustomerAndDevicesDto findCustomerByPhoneNumber(Long phoneNumber){
        Customer c = customerRepository.getCustomerWithDevicesByPhoneNumber(phoneNumber).orElseThrow(()->
                new IllegalArgumentException("Customer with number "+ phoneNumber+" not found"));
        List<CustomerAndDevicesDto.DeviceDto> deviceDtos = c.getDevices().stream()
                .map(device -> modelMapper.map(device, CustomerAndDevicesDto.DeviceDto.class))
                .toList();

        return CustomerAndDevicesDto.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .phoneNumber(c.getPhoneNumber())
                .devices(deviceDtos)
                .build();
    }
    public Customer findCustomerByIdWithDetails(Long id){

        return customerRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with id " + id +" not found"));
    }
    public List<CustomerInfoDto> findAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerInfoDto> customerDto = new ArrayList<>();
        for(Customer c:customers){
            customerDto.add(modelMapper.map(c, CustomerInfoDto.class));
        }
        return customerDto;
    }


  public CustomerInfoDto editCustomerInfo(Long id,CustomerInfoDto customerInfoDto){
    Customer customer = customerRepository.getCustomerById(id).orElseThrow();
    if(!customerInfoDto.getPhoneNumber().equals(customer.getPhoneNumber())&&
            customerRepository.getCustomerByPhoneNumber(customerInfoDto.getPhoneNumber()).isPresent()){

throw new IllegalArgumentException("Phone number taken");
  }else {
        if(customerInfoDto.getPhoneNumber()!=null)
            customer.setPhoneNumber(customerInfoDto.getPhoneNumber());
        if(customerInfoDto.getFirstName()!=null)
            customer.setFirstName(customerInfoDto.getFirstName());
        if(customerInfoDto.getLastName()!=null)
            customer.setLastName(customerInfoDto.getLastName());
    }
        return modelMapper.map(customerRepository.save(customer),CustomerInfoDto.class);
        }




@Transactional
    public boolean deleteCustomerById(Long id) {
        if(customerRepository.existsById(id)){
            customerRepository.deleteById(id);
            return true;
        }
        else
            return false;
    }




    @Transactional
    @CacheEvict(value = "services", allEntries = true)
    public CustomerCreationDto2 createCustomer3ipol4(CustomerCreationDto2 customerDto){
        if(customerDto.getDevice().getDeviceType()==null){
            throw new IllegalArgumentException("device type needed");//todo enum validator nedded
        }
        Customer handledCustomer = customerRepository.getCustomerWithDevicesByPhoneNumber(customerDto.getCustomer().getPhoneNumber())
                .orElseGet(() -> createNewCustomer(customerDto));
        if (customerDto.getDevice() != null) {
            handleDevice(customerDto, handledCustomer);
        }
        Customer c= customerRepository.save(handledCustomer);

        CustomerCreationDto2 mapped = CustomerCreationDto2
                .builder()
                .customer(CustomerCreationDto2.
                        CustomerCreationDto
                        .builder()
                        .id(c.getId())
                        .firstName(c.getFirstName())
                        .lastName(c.getLastName())
                        .phoneNumber(c.getPhoneNumber()).build())
                .build();
        String deviceSerialNumber = customerDto.getDevice().getDeviceSerialNumber();
        Device matchedDevice = c.getDevices().stream()
                .filter(device -> device.getDeviceSerialNumber().equals(deviceSerialNumber))
                .findFirst()
                .orElse(null);

        mapped.setDevice(modelMapper.map(matchedDevice, CustomerCreationDto2.DeviceCreationDto.class));
        List<ServiceRequest> serviceRequests = c.getDevices().get(0).getServiceRequestList();
        ServiceRequest mappedRequest = serviceRequests.get(serviceRequests.size() - 1);
        mapped.setServiceRequest(modelMapper.map(mappedRequest, CustomerCreationDto2.ServiceRequestCreationDto.class));

        return mapped;
    }

    private Customer createNewCustomer(CustomerCreationDto2 customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getCustomer().getFirstName());
        customer.setLastName(customerDto.getCustomer().getLastName());
        customer.setPhoneNumber(customerDto.getCustomer().getPhoneNumber());
        customer.setDevices(new ArrayList<>());
        return customer;
    }

    private void handleDevice(CustomerCreationDto2 customerDto, Customer handledCustomer) {
        Optional<Device>existingDevice =handledCustomer.getDevices().stream()
                .filter(device -> device.getDeviceSerialNumber().equals(customerDto.getDevice().getDeviceSerialNumber()))
                .findFirst();

        if (customerDto.getServiceRequest() != null) {
            handleServiceRequest(customerDto, handledCustomer, existingDevice);
        } else {
            addDeviceWithoutServiceRequest(customerDto, handledCustomer,existingDevice);
        }
    }

    private void handleServiceRequest(CustomerCreationDto2 customerDto, Customer handledCustomer, Optional<Device> existingDevice) {
        ServiceRequest newService = createNewService(customerDto);
        Device deviceToPersist;
        deviceToPersist = existingDevice.orElseGet(() -> buildDevice(customerDto.getDevice()));
        newService.setDevice(deviceToPersist);
        deviceToPersist.getServiceRequestList().add(newService);
        deviceToPersist.setCustomer(handledCustomer);
        handledCustomer.getDevices().add(deviceToPersist);
    }

    private ServiceRequest createNewService(CustomerCreationDto2 customerDto) {
           User u = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());
        ServiceRequest newService = ServiceRequest.builder()
                .startDate(LocalDate.now())
                .lastStatus(ServiceRequest.Status.PENDING)
                .description(customerDto.getServiceRequest().getDescription())
                .price(customerDto.getServiceRequest().getPrice())
                .user(u)
                .build();

        List<StatusHistory> list = new ArrayList<>();
        StatusHistory status = new StatusHistory(ServiceRequest.Status.PENDING, "Request created", newService, LocalDateTime.now());
        list.add(status);
        newService.setStatusHistory(list);
        return newService;
    }

    private void addDeviceWithoutServiceRequest(CustomerCreationDto2 customerDto, Customer handledCustomer,Optional<Device> existingDevice) {
        if (existingDevice.isEmpty()) {
            Device deviceToPersist = buildDevice(customerDto.getDevice());
            handledCustomer.getDevices().add(deviceToPersist);
            deviceToPersist.setCustomer(handledCustomer);
        }
    }

private Device buildDevice(CustomerCreationDto2.DeviceCreationDto deviceDto){

    return Device.builder()
            .deviceName(deviceDto.getDeviceName())
            .deviceType(deviceDto.getDeviceType())
            .deviceSerialNumber(deviceDto.getDeviceSerialNumber())
            .serviceRequestList(new ArrayList<>())
            .build();
}
}
