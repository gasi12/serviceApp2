package com.example.serviceApp.customer;

import com.example.serviceApp.customer.Dto.CustomerDto;
import com.example.serviceApp.customer.Dto.CustomerWithRequestsDto;
import com.example.serviceApp.serviceRequest.ServiceRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
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
    public Customer createNewCustomer(Customer newCustomer) {
        String password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        newCustomer.setPassword(passwordEncoder.encode(password));
        updateCustomerServiceRequests(newCustomer);
        Customer savedCustomer = customerRepository.save(newCustomer);
        savedCustomer.setPlainPassword(password);
        return savedCustomer;
    }

    public Customer updateExistingCustomer(Customer existingCustomer, Customer updatedCustomer) {
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        updateCustomerServiceRequests(existingCustomer);
        return customerRepository.save(existingCustomer);
    }

    private void updateCustomerServiceRequests(Customer customer) {
        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();
        for (ServiceRequest s : serviceRequestList) {
            s.setCustomer(customer);
            s.setStatus(ServiceRequest.Status.PENDING);
        }
    }
    public Customer createCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
        return existingCustomer.isPresent()
                ? updateExistingCustomer(existingCustomer.get(), customer)
                : createNewCustomer(customer);
    }
//    @Transactional
//    public Customer createCustomer(Customer customer) {
//        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
//        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();
//
//        Customer handledCustomer;
//        String password = null;
//
//        if (existingCustomer.isPresent()) {
//            handledCustomer = existingCustomer.get();
//        } else {
//            password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
//            customer.setPassword(passwordEncoder.encode(password));
//            handledCustomer = customer;
//        }
//
//        handledCustomer.setFirstName(customer.getFirstName());
//        handledCustomer.setServiceRequestList(serviceRequestList);
//
//        for (ServiceRequest s : serviceRequestList) {
//            s.setCustomer(handledCustomer);
//            s.setStatus(ServiceRequest.Status.PENDING);
//        }
//
//        Customer savedCustomer = customerRepository.save(handledCustomer);
//
//        if(password != null) {
//            savedCustomer.setPlainPassword(password);
//        }
//
//        return savedCustomer;
//    }

@Transactional
    public Customer editCustomerById(Long id, CustomerDto customer){
        Customer editedCustomer = customerRepository.getCustomerById(id).orElseThrow(()-> new IllegalArgumentException("customer with id " + id + " not found"));
        if(!customer.getFirstName().isBlank())
            editedCustomer.setFirstName(customer.getFirstName());
        if(!customer.getLastName().isBlank())
            editedCustomer.setLastName(customer.getLastName());
        if(customer.getPhoneNumber()!=null)
            editedCustomer.setPhoneNumber(customer.getPhoneNumber());
       return customerRepository.save(editedCustomer);
    }

    public boolean deleteCustomerById(Long id) {
        if(customerRepository.existsById(id)){
            customerRepository.deleteById(id);
            return true;
        }
        else
            return false;
    }
}
