package com.example.serviceApp.customer;

import com.example.serviceApp.EmailServiceImpl;
import com.example.serviceApp.customer.Dto.CustomerAndServiceCreationDto;
import com.example.serviceApp.customer.Dto.CustomerInfoDto;
import com.example.serviceApp.customer.Dto.CustomerAndRequestDto;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import com.google.common.cache.Cache;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        private final PasswordEncoder passwordEncoder;
        private final EmailServiceImpl emailService;
        private final Cache<Long, List<String>> userCache;

        public void sendEmail(){
            emailService.sendEmail("gussy1258@gmail.com","TO JEST MAIL Z APKI","Brawo");
        }

        public CustomerAndRequestDto findCustomerById(Long id){


        return modelMapper.map(customerRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with id " + id +" not found")), CustomerAndRequestDto.class);
    }
    public Customer findCustomerByIdWithDetails(Long id){
            log.info("A");
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


    public CustomerAndRequestDto findCustomerByPhoneNumber(Long phoneNumber){
        return modelMapper.map(
                customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()->
                        new IllegalArgumentException("user with number "+ phoneNumber + "does not exist")),
                CustomerAndRequestDto.class);

    }
    public Customer createCustomer2(CustomerAndServiceCreationDto customerDto) {
        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customerDto.getPhoneNumber());
        User u = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());

        Customer handledCustomer;
        if(existingCustomer.isPresent()){
            handledCustomer=existingCustomer.get();
        }
        else{
            handledCustomer = modelMapper.map(customerDto,Customer.class);
//            String password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
//            handledCustomer.setPlainPassword(password);
//            handledCustomer.setPassword(passwordEncoder.encode(password));
            handledCustomer.setIsPhoneNumberVerified(false);
        }

        List<ServiceRequest> newRequests = customerDto.getServiceRequestList().stream()
                .map((element) -> modelMapper.map(element, ServiceRequest.class))
                .toList();

        List<ServiceRequest> existingRequests = handledCustomer.getServiceRequestList();
        if (existingRequests == null) {
            existingRequests = new ArrayList<>();
        } else {
            existingRequests.clear();
        }
        existingRequests.addAll(newRequests);

        for(ServiceRequest n: existingRequests){
            n.setUser(u);
            n.setCustomer(handledCustomer);
            List<StatusHistory> list = n.getStatusHistory();
            if (list == null) {
                list = new ArrayList<>();
            }
            StatusHistory s = new StatusHistory(ServiceRequest.Status.PENDING,"Request created",n, LocalDateTime.now());
            list.add(s);
            n.setStatusHistory(list);
        }

        handledCustomer.setServiceRequestList(existingRequests);
        Customer savedCustomer = customerRepository.save(handledCustomer);

        for(ServiceRequest s: savedCustomer.getServiceRequestList()){
            userCache.put(s.getId(), List.of(s.getCustomer().getUsername(),s.getUser().getUsername()));
        }

        return savedCustomer;
    }

    @Transactional
    public CustomerInfoDto editCustomerById(Long id, CustomerInfoDto customer){
        Customer editedCustomer = customerRepository.getCustomerById(id).orElseThrow(()-> new IllegalArgumentException("customer with id " + id + " not found"));
        if(!customer.getFirstName().isBlank())//todo czy mozna to z automatu?
            editedCustomer.setFirstName(customer.getFirstName());
        if(!customer.getLastName().isBlank())
            editedCustomer.setLastName(customer.getLastName());
        if(customer.getPhoneNumber()!=null)
            editedCustomer.setPhoneNumber(customer.getPhoneNumber());
        return modelMapper.map(customerRepository.save(editedCustomer), CustomerInfoDto.class);

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



}
