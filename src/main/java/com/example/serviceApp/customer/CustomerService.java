package com.example.serviceApp.customer;

import com.example.serviceApp.serviceRequest.ServiceRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerDto findUserById(Long id){
        return modelMapper.map(customerRepository.findById(id).orElseThrow(),CustomerDto.class);
    }
    public Customer findUserByIdWithDetails(Long id){
        return customerRepository.findById(id).orElseThrow();
    }
    public List<CustomerDto> findAllUsers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDto = new ArrayList<>();
        for(Customer c:customers){
            customerDto.add(modelMapper.map(c,CustomerDto.class));
        }
        return customerDto;
    }

    public CustomerDto findUserByPhoneNumber(Long phoneNumber){
        return modelMapper.map(
                customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()->
                        new IllegalArgumentException("not found")),
                CustomerDto.class);
    }
    @Transactional

    public Customer createUserWithoutChecking(Customer customer) {
        Optional<Customer> newUser = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();
        if (!newUser.isPresent()) {
            for (ServiceRequest s : serviceRequestList) {
                s.setCustomer(customer);
                s.setStatus(ServiceRequest.Status.PENDING);
            }
            customer.setServiceRequestList(serviceRequestList);
            return customerRepository.save(customer);
        }
        Customer newCustomer = newUser.get();
        for (ServiceRequest s : serviceRequestList) {
            s.setCustomer(newCustomer);
            s.setStatus(ServiceRequest.Status.PENDING);
            newCustomer.getServiceRequestList().add(s);
        }
        newCustomer.setServiceRequestList(serviceRequestList);
        return  customerRepository.save(newCustomer);

    }
    @Transactional
    public Customer editUserById(Long id, CustomerDto user){
        Customer editedUser = customerRepository.getCustomerById(id).orElseThrow(()-> new IllegalArgumentException("user not found"));
        if(!user.getUserName().isBlank())
            editedUser.setUserName(user.getUserName());
        if(user.getPhoneNumber()!=null)
            editedUser.setPhoneNumber(user.getPhoneNumber());
       return customerRepository.save(editedUser);
    }
}
