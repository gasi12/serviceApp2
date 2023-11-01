package com.example.serviceApp.security.auth;


import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.security.auth.Dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    @PostMapping("/register/user")
    public ResponseEntity register(@RequestBody RegisterRequest request){
        AuthenticationResponse response = authService.register(request);
        if (response == null) {
            return ResponseEntity.badRequest().body("USER EXIST"); // Return bad request if email is already taken
        }
        return ResponseEntity.ok(response); // Return ok response with AuthenticationResponse
    }

    @PostMapping("/authenticate/user")
    public ResponseEntity authenticateUser(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authService.authenticate(request, userRepository::findByEmail);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/authenticate/customer")
    public ResponseEntity authenticateCustomer(@RequestBody AuthenticationRequest request){
        Function<String, Optional<? extends UserImplementation>> finder = userName->{
            try {
                Long phoneNumber = Long.parseLong(request.getUsername());
                return customerRepository.findByPhoneNumber(phoneNumber);
            }
            catch (NumberFormatException e){
                throw new UsernameNotFoundException("Username not found "+ request.getUsername());
            }
        };
        AuthenticationResponse response = authService.authenticate(request,finder);
        return ResponseEntity.ok(response);
    }
//@PostMapping("/authenticate/user")
//public ResponseEntity authenticate(@RequestBody AuthenticationRequest request){
//    AuthenticationResponse response = authService.authenticateUser(request);
//    return ResponseEntity.ok(response);
//}

//    @PostMapping("/authenticate/customer")
//    public ResponseEntity authenticateCustomer(@RequestBody CustomerAuthenticationRequest request){
//        log.info("przed resoponse");
//        AuthenticationResponse response = authService.authenticateCustomer(request);
//        log.info("Logged user:" +SecurityContextHolder.getContext().getAuthentication().getName());
//        if (response == null) {
//            return ResponseEntity.badRequest().body("Password expired"); //todo na razie zadrutowane tak zeby dostac odpowiedz zamiast 403
//        }
//        log.info("response:\n");
//        return ResponseEntity.ok(response);
//    }
    @PostMapping("/password/user")
    public User changeUserPassword(@RequestBody PasswordChangeRequest request){

        return authService.changeUserPassword(request);
    }
    @PostMapping("/password/customer")
    public Customer changeCustomerPassword(@RequestBody PasswordChangeRequest request){

        return authService.changeCustomerPassword(request);
    }
    @PostMapping("/refresh")
    public ResponseEntity refreshAuthentication(@RequestBody TokenRequest request){
        return authService.refreshAuthentication(request);
    }
}
