package com.example.serviceApp.Customer;

import com.example.serviceApp.serviceRequest.ServiceRequest;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service

public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer findUserById(Long id){
        return customerRepository.findById(id).orElseThrow();
    }
    public List<Customer> findAllUsers(){
        return customerRepository.findAll();
    }

    public Customer findUserByPhoneNumber(Long phoneNumber){return customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new IllegalArgumentException("not found"));}
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
        if(!user.getName().isBlank())
            editedUser.setUserName(user.getName());
        if(user.getPhoneNumber()!=null)
            editedUser.setPhoneNumber(user.getPhoneNumber());
       return customerRepository.save(editedUser);
    }
}
