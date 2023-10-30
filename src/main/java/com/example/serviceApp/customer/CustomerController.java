package com.example.serviceApp.customer;

import com.example.serviceApp.customer.Dto.CustomerDtoWithTempPassword;
import com.example.serviceApp.chat.ChatTicket;
import com.example.serviceApp.chat.TicketService;
import com.example.serviceApp.customer.Dto.CustomerDto;
import com.example.serviceApp.customer.Dto.CustomerWithRequestsDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @GetMapping("/generateticket")//todo postawic to w jakis madrzejszy controller, tu dziala ale jest burdel ale sam kontroelr działajet
        public ChatTicket generateTicket() {
        return new ChatTicket(TicketService.generateAndStoreTicket());
    }


    @PostMapping("/customer")//ok
    public CustomerDtoWithTempPassword createCustomerDumb(@RequestBody Customer customer) {
        return modelMapper.map(customerService.createCustomer2(customer), CustomerDtoWithTempPassword.class);
    }

    @GetMapping("/customer/{id}")//ok
    public CustomerWithRequestsDto findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);

    }
    @GetMapping("/customer/{id}/details")//ok todo development only!!!
    public Customer getCustomerByIdAll(@PathVariable Long id) {
        return customerService.findCustomerByIdWithDetails(id);
    }

    @PutMapping("/customer/{id}")//ok
    public CustomerDto editCustomerById(@PathVariable Long id, @RequestBody CustomerDto customer) {
        return customerService.editCustomerById(id, customer);
    }

    @GetMapping("/customer/phonenumber/{number}")//ok
    public CustomerWithRequestsDto findCustomerByPhoneNumber(@PathVariable Long number) {
        return customerService.findCustomerByPhoneNumber(number);
    }

    @GetMapping("/customer/getall")//ok
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