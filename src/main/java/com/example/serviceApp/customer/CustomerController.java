package com.example.serviceApp.customer;

import com.example.serviceApp.chat.ChatTicket;
import com.example.serviceApp.chat.TicketHandshakeInterceptor;
import com.example.serviceApp.customer.Dto.CustomerDto;
import com.example.serviceApp.customer.Dto.CustomerWithRequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/generateticket")//todo postawic to w jakis madrzejszy controller, tu dziala ale jest burdel
        public ChatTicket generateTicket() {
        return new ChatTicket(TicketHandshakeInterceptor.generateAndStoreTicket());
    }


    @PostMapping("/customer")
    public Customer createCustomerDumb(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/customer/{id}")
    public CustomerWithRequestsDto findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);

    }
    @GetMapping("/customer/{id}/details")
    public Customer getCustomerByIdAll(@PathVariable Long id) {
        return customerService.findCustomerByIdWithDetails(id);
    }

    @PutMapping("/customer/{id}")
    public Customer editCustomerById(@PathVariable Long id, @RequestBody CustomerDto customer) {
        return customerService.editCustomerById(id, customer);
    }

    @GetMapping("/customer/phonenumber/{number}")
    public CustomerWithRequestsDto findCustomerByPhoneNumber(@PathVariable Long number) {
        return customerService.findCustomerByPhoneNumber(number);
    }

    @GetMapping("/customer/getall")
    public List<CustomerDto> findAllCustomers() {
        return customerService.findAllCustomers();
    }
    @DeleteMapping("/customer/{id}")
    public ResponseEntity deleteCustomer(@PathVariable Long id){
        if(customerService.deleteCustomerById(id)){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.badRequest().build();
    }
}