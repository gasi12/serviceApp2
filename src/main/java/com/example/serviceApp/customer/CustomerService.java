package com.example.serviceApp.customer;

import com.example.serviceApp.EmailServiceImpl;
import com.example.serviceApp.customer.Dto.CustomerDto;
import com.example.serviceApp.customer.Dto.CustomerWithRequestsDto;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.google.common.cache.Cache;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        public CustomerWithRequestsDto findCustomerById(Long id){


        return modelMapper.map(customerRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with id " + id +" not found")),CustomerWithRequestsDto.class);
    }
    public Customer findCustomerByIdWithDetails(Long id){
            log.info("A");
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
public Customer createCustomer2(Customer customer){
    Optional<Customer> existingCustomer = customerRepository.getCustomerByPhoneNumber(customer.getPhoneNumber());
    User u= userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new IllegalArgumentException());
    Customer handledCustomer;


    if(existingCustomer.isPresent()){
        handledCustomer=existingCustomer.get();
    }
    else{
        String password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        handledCustomer=customer;
        customer.setPlainPassword(password);
        customer.setPassword(passwordEncoder.encode(password));
    }

    for(ServiceRequest s :customer.getServiceRequestList()){
        s.setUser(u);
        s.setCustomer(handledCustomer);
    }
    handledCustomer.setServiceRequestList(customer.getServiceRequestList());
          Customer savedCustomer= customerRepository.save(handledCustomer);
          for(ServiceRequest s: savedCustomer.getServiceRequestList()){
              userCache.put(s.getId(), List.of(s.getCustomer().getUsername(),s.getUser().getUsername()));
          }
          return savedCustomer;

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
