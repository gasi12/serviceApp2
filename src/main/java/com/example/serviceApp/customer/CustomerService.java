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
        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();

        Customer handledCustomer;
        String password = null;
        if (existingCustomer.isPresent()) {
            handledCustomer = existingCustomer.get();
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
            // Bcrypt the generated password and set it to customer
            customer.setPassword(passwordEncoder.encode(password));

            handledCustomer = customer;
            handledCustomer.setServiceRequestList(serviceRequestList);

        }

        Customer savedCustomer = customerRepository.save(handledCustomer);
        if(password!=null)
            savedCustomer.setPlainPassword(password);
        // Assuming 'plainPassword' attribute to store plain password in customer response object.

        return  savedCustomer;
    }

    @Transactional
    public Customer editUserById(Long id, CustomerDto user){
        Customer editedUser = customerRepository.getCustomerById(id).orElseThrow(()-> new IllegalArgumentException("user not found"));
        if(!user.getCustomerName().isBlank())
            editedUser.setCustomerName(user.getCustomerName());
        if(user.getPhoneNumber()!=null)
            editedUser.setPhoneNumber(user.getPhoneNumber());
       return customerRepository.save(editedUser);
    }
}
