package com.example.serviceApp.customer;

import com.example.serviceApp.chat.ChatTicket;
import com.example.serviceApp.chat.TicketService;
import com.example.serviceApp.customer.Dto.CustomerAndServiceCreationDto;
import com.example.serviceApp.customer.Dto.CustomerInfoDto;
import com.example.serviceApp.customer.Dto.CustomerDtoWithTempPassword;
import com.example.serviceApp.customer.Dto.CustomerAndRequestDto;
import com.google.common.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final Cache<Long, List<String>> userCache;

    @GetMapping("/cache")//todo testowe
    public Map<Long, List<String>> getCache(){
        return  userCache.asMap();
    }
    @GetMapping("/generateticket")//todo postawic to w jakis madrzejszy controller, tu dziala ale jest burdel ale sam kontroelr działajet
        public ChatTicket generateTicket() {
        return new ChatTicket(TicketService.generateAndStoreTicket());
    }
    @PostMapping("/mail")
    public void sendEmail(){

                customerService.sendEmail();
    }

    @PostMapping("/customer")//ok now
    public CustomerDtoWithTempPassword createCustomerDumb(@RequestBody CustomerAndServiceCreationDto customer) {
        return modelMapper.map(customerService.createCustomer2(customer), CustomerDtoWithTempPassword.class);
    }

    @GetMapping("/customer/{id}")//ok now
    public CustomerAndRequestDto findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);

    }
    @GetMapping("/customer/{id}/details")//ok todo development only!!!
    public Customer getCustomerByIdAll(@PathVariable Long id) {
        return customerService.findCustomerByIdWithDetails(id);
    }

    @PutMapping("/customer/{id}")//ok now
    public CustomerInfoDto editCustomerById(@PathVariable Long id, @RequestBody CustomerInfoDto customer) {
        return customerService.editCustomerById(id, customer);
    }

    @GetMapping("/customer/phonenumber/{number}")//ok
    public CustomerAndRequestDto findCustomerByPhoneNumber(@PathVariable Long number) {
        return customerService.findCustomerByPhoneNumber(number);
    }

    @GetMapping("/customer/getall")//ok
    public List<CustomerInfoDto> findAllCustomers() {
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