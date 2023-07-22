package com.example.serviceApp.customer;

import com.example.serviceApp.serviceRequest.ServiceRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerWithRequestsDto findCustomerById(Long id){
        return modelMapper.map(customerRepository.findById(id).orElseThrow(),CustomerWithRequestsDto.class);
    }
    public Customer findCustomerByIdWithDetails(Long id){
        return customerRepository.findById(id).orElseThrow();
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
                        new IllegalArgumentException("not found")),
                CustomerWithRequestsDto.class);
    }
    @Transactional

    public Customer createCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();

        Customer handledCustomer = new Customer();

        String password = null;
        if (existingCustomer.isPresent()) {
            handledCustomer = existingCustomer.get();
            handledCustomer.setCustomerName(customer.getCustomerName());
            for (ServiceRequest s : serviceRequestList) {
                s.setCustomer(handledCustomer);
                s.setStatus(ServiceRequest.Status.PENDING);
                handledCustomer.getServiceRequestList().add(s);
            }
        } else {
        for (ServiceRequest s : serviceRequestList) {
                s.setCustomer(customer);
                s.setStatus(ServiceRequest.Status.PENDING);
            }
            password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            customer.setPassword(passwordEncoder.encode(password));
            handledCustomer = customer;
            handledCustomer.setCustomerName(customer.getCustomerName());
            handledCustomer.setServiceRequestList(serviceRequestList);
        }

        Customer savedCustomer = customerRepository.save(handledCustomer);
        if(password!=null)
            savedCustomer.setPlainPassword(password);
        return  savedCustomer;
    }

    @Transactional
    public Customer editCustomerById(Long id, CustomerDto customer){
        Customer editedCustomer = customerRepository.getCustomerById(id).orElseThrow(()-> new IllegalArgumentException("customer not found"));
        if(!customer.getCustomerName().isBlank())
            editedCustomer.setCustomerName(customer.getCustomerName());
        if(customer.getPhoneNumber()!=null)
            editedCustomer.setPhoneNumber(customer.getPhoneNumber());
       return customerRepository.save(editedCustomer);
    }
}
