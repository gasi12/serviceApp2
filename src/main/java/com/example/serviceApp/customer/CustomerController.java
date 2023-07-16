package com.example.serviceApp.customer;

import com.example.serviceApp.chat.TicketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/generateticket")//todo postawic to w jakis madrzejszy controller, tu dziala ale jest burdel
    public String generateTicket() {

        return TicketHandshakeInterceptor.generateAndStoreTicket();
    }


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