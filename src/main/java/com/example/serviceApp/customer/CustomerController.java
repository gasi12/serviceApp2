package com.example.serviceApp.customer;

import com.example.serviceApp.chat.ChatTicket;
import com.example.serviceApp.chat.TicketService;
import com.example.serviceApp.customer.Dto.devicedto.*;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.ServiceRequestService;
import com.google.common.cache.Cache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;
    private final ServiceRequestService serviceRequestService;
//    @GetMapping("/cache")//todo testowe
//    public Map<Long, List<String>> getCache(){
//        return  userCache.asMap();
//    }
@Operation(summary = "do czatu, na razie olać")
    @GetMapping("/generateticket")//todo postawic to w jakis madrzejszy controller, tu dziala ale jest burdel ale sam kontroelr działajet
        public ChatTicket generateTicket() {
        return new ChatTicket(TicketService.generateAndStoreTicket());
    }
//    @PostMapping("/mail")
//    public void sendEmail(){
//
//                customerService.sendEmail();
//    }
    @Operation(summary = "response jest zły, ale dodaje dobrze, olać na razie")
    @PostMapping("/customer")//ok now
    public CustomerCreationDto2 createCustomerDumb(@RequestBody  @Valid CustomerCreationDto2 customer) {
        return customerService.createCustomer3ipol4(customer);
    }

    @GetMapping("/customer/id/{id}")//ok now
    public CustomerAndDevicesDto findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }
    @GetMapping("/customer/phoneNumber/{phoneNumber}")//ok now
    public CustomerAndDevicesDto findCustomerByPhoneNumber(@PathVariable Long phoneNumber) {
        return customerService.findCustomerByPhoneNumber(phoneNumber);
    }
    @Operation(summary = "to sluzy do podgladu co sie dzieje pod maska, nie uzywaj tego na frontendzie")
    @GetMapping("/customer/{id}/details")// todo development only!!!
    public Customer getCustomerByIdAll(@PathVariable Long id) {
        return customerService.findCustomerByIdWithDetails(id);
    }

    @PutMapping("/customer/{id}")//ok now
    public CustomerInfoDto editCustomerById(@PathVariable Long id, @RequestBody CustomerInfoDto customer) {
        return customerService.editCustomerInfo(id, customer);
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