package com.example.serviceApp.security.auth;


import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.security.auth.Dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
@RequestMapping( "/auth")
@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping()
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/getusers")
    public List<User> getUsers(){
       return userRepository.findAll();

    }
    @PostMapping("/register/user")
    public AuthenticationResponse register(@RequestBody CustomerRegisterRequest request){
        AuthenticationResponse response = authService.register(request);


       return response; // Return ok response with AuthenticationResponse
    }

    @PostMapping("/authenticate/user")
    public AuthenticationResponse authenticateUser(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authService.authenticate(request, userRepository::findByEmail);
        return response;
    }
    @PostMapping("/authenticate/customer")
    public AuthenticationResponse authenticateCustomer(@RequestBody AuthenticationRequest request){
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
        return response;
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
    public AuthenticationResponse refreshAuthentication(@RequestBody RefreshTokenRequest request){
        return authService.refreshAuthentication(request);
    }
    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@RequestBody AuthenticationRequest request)throws IOException, InterruptedException {
        if (authService.registerCustomer(request)) {
            return ResponseEntity.ok().body("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Unable to register user");
        }
    }
    @PostMapping("/register/customer/verify")
    public ResponseEntity<String> verifyCustomerPhoneNumber(@RequestBody AuthenticationRequest request) throws IOException, InterruptedException{
        if (authService.validateToken(request)) {
            return ResponseEntity.ok().body("User verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Unable to verify");
        }
    }

}
