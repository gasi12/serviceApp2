package com.example.serviceApp.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {


    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return customerRepository.findByPhoneNumber(Long.parseLong(phoneNumber))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
        //return new User(customer.getPhoneNumber().toString(), customer.getPassword(), new ArrayList<>());
    }
}