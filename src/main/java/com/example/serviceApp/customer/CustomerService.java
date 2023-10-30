package com.example.serviceApp.customer;

import com.example.serviceApp.customer.Dto.CustomerDto;
import com.example.serviceApp.customer.Dto.CustomerWithRequestsDto;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.ServiceRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
    @RequiredArgsConstructor
    public class CustomerService {

        private final CustomerRepository customerRepository;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;
        private final PasswordEncoder passwordEncoder;
        private final ConcurrentHashMap<Long, List<String>> userOrdersCache;
        public CustomerWithRequestsDto findCustomerById(Long id){

        return modelMapper.map(customerRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with id " + id +" not found")),CustomerWithRequestsDto.class);
    }
    public Customer findCustomerByIdWithDetails(Long id){
        return customerRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with id " + id +" not found"));
    }
    public List<CustomerDto> findAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDto = new ArrayList<>();
        for(Customer c:customers){
            customerDto.add(modelMapper.map(c,CustomerDto.class));
        }
        return customerDto;
    }

    public CustomerWithRequestsDto findCustomerByPhoneNumber(Long phoneNumber){
        return modelMapper.map(
                customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()->
                        new IllegalArgumentException("user with number "+ phoneNumber + "does not exist")),
                CustomerWithRequestsDto.class);
    }
    @Transactional//todo user sieje ferment
    public Customer createCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();

        Customer handledCustomer;
        String password = null;

        if (existingCustomer.isPresent()) {
            handledCustomer = existingCustomer.get();
        } else {
            password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            customer.setPassword(passwordEncoder.encode(password));
            handledCustomer = customer;
        }

        handledCustomer.setFirstName(customer.getFirstName());
        handledCustomer.setServiceRequestList(serviceRequestList);
        handledCustomer.setRole(Role.CUSTOMER);

       User u= userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());
        u = userRepository.saveAndFlush(u);
        for (ServiceRequest s : serviceRequestList) {
            s.setCustomer(handledCustomer);
            s.setStatus(ServiceRequest.Status.PENDING);
            s.setUser(u);

        }


        Customer savedCustomer = customerRepository.save(handledCustomer);

        if(password != null) {
            savedCustomer.setPlainPassword(password);
        }
        for (ServiceRequest s : savedCustomer.getServiceRequestList()) {
            List<String> values =userOrdersCache.computeIfAbsent(s.getId(), k -> new ArrayList<>());
            if (!values.contains(s.getCustomer().getUsername())) {
                values.add(s.getCustomer().getUsername());
            }
//            if (!values.contains(u.getUsername())) {
//                values.add(u.getUsername());
//            }
            log.info(s.getId().toString());

        }

        return savedCustomer;
    }
public Customer createCustomer2(Customer customer){
    Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
    User u= userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());
    Customer handledCustomer;
    String password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

    if(existingCustomer.isPresent()){
        handledCustomer=existingCustomer.get();
    }
    else{

        handledCustomer=customer;
        customer.setPlainPassword(password);
        customer.setPassword(passwordEncoder.encode(password));
    }

    for(ServiceRequest s :customer.getServiceRequestList()){
        s.setUser(u);
        s.setCustomer(handledCustomer);
    }
    handledCustomer.setServiceRequestList(customer.getServiceRequestList());

          return   customerRepository.save(handledCustomer);

}
    @Transactional
    public CustomerDto editCustomerById(Long id, CustomerDto customer){
        Customer editedCustomer = customerRepository.getCustomerById(id).orElseThrow(()-> new IllegalArgumentException("customer with id " + id + " not found"));
        if(!customer.getFirstName().isBlank())//todo czy mozna to z automatu?
            editedCustomer.setFirstName(customer.getFirstName());
        if(!customer.getLastName().isBlank())
            editedCustomer.setLastName(customer.getLastName());
        if(customer.getPhoneNumber()!=null)
            editedCustomer.setPhoneNumber(customer.getPhoneNumber());
        return modelMapper.map(customerRepository.save(editedCustomer),CustomerDto.class);
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
