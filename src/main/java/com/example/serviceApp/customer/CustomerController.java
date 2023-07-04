package com.example.serviceApp.customer;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping("/savecustomerdumb")
    public Customer createCustomerDumb(@RequestBody Customer customer) {
        return customerService.createUserWithoutChecking(customer);
    }

    @GetMapping("/user/{id}")
    public CustomerDto findUserById(@PathVariable Long id) {
        return customerService.findUserById(id);

    }
    @GetMapping("/user/{id}/details")
    public Customer getUserByIdAll(@PathVariable Long id) {
        return customerService.findUserByIdWithDetails(id);
    }

    @PutMapping("/user/{id}")
    public Customer editUserById(@PathVariable Long id, @RequestBody CustomerDto user) {
        return customerService.editUserById(id, user);
    }

    @GetMapping("/user/phonenumber/{number}")
    public CustomerDto findUserByPhoneNumber(@PathVariable Long number) {
        return customerService.findUserByPhoneNumber(number);
    }

    @GetMapping("/user/getall")
    public List<CustomerDto> findAllUsers() {
        return customerService.findAllUsers();
    }
}