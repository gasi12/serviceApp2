package com.example.serviceApp.customer;

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


//    public Customer createCustomer(Customer customer) {
//        Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
//        List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();
//
//        Customer handledCustomer;
//
//        String password = null;
//        if (existingCustomer.isPresent()) {
//            handledCustomer = existingCustomer.get();
//            handledCustomer.setFirstName(customer.getFirstName());
//            for (ServiceRequest s : serviceRequestList) {
//                s.setCustomer(handledCustomer);
//                s.setStatus(ServiceRequest.Status.PENDING);
//                handledCustomer.getServiceRequestList().add(s);
//            }
//        } else {
//        for (ServiceRequest s : serviceRequestList) {
//                s.setCustomer(customer);
//                s.setStatus(ServiceRequest.Status.PENDING);
//            }
//            password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
//            customer.setPassword(passwordEncoder.encode(password));
//            handledCustomer = customer;
//            handledCustomer.setFirstName(customer.getFirstName());
//            handledCustomer.setServiceRequestList(serviceRequestList);
//        }
//
//        Customer savedCustomer = customerRepository.save(handledCustomer);
//        if(password!=null)
//            savedCustomer.setPlainPassword(password);
//        return  savedCustomer;
//    }
@Transactional
public Customer createCustomer(Customer customer) {
    List<ServiceRequest> serviceRequestList = customer.getServiceRequestList();
    serviceRequestList.forEach(s -> s.setStatus(ServiceRequest.Status.PENDING));

    Customer handledCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber())
            .map(existingCustomer -> updateExistingCustomer(existingCustomer, serviceRequestList))
            .orElseGet(() -> createNewCustomer(customer, serviceRequestList));

    return customerRepository.save(handledCustomer);
}


    private Customer updateExistingCustomer(Customer existingCustomer, List<ServiceRequest> serviceRequestList) {
        serviceRequestList.forEach(s -> {
            s.setCustomer(existingCustomer);
            existingCustomer.getServiceRequestList().add(s);
        });

  return existingCustomer;
    }
    private Customer createNewCustomer(Customer newCustomer, List<ServiceRequest> serviceRequestList) {
        String password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        newCustomer.setPassword(passwordEncoder.encode(password));
        newCustomer.setPlainPassword(password);
        newCustomer.setServiceRequestList(serviceRequestList);
        serviceRequestList.forEach(s -> s.setCustomer(newCustomer));
        return newCustomer;
    }
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
