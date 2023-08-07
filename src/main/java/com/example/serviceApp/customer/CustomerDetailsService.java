package com.example.serviceApp.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByPhoneNumber(Long.parseLong(phoneNumber))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
return customer;
        //return new User(customer.getPhoneNumber().toString(), customer.getPassword(), new ArrayList<>());
    }
}