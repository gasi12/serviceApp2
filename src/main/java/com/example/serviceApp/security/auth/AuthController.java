package com.example.serviceApp.security.auth;


import com.example.serviceApp.customExeptions.PasswordChangeRequiredException;
import com.example.serviceApp.customer.CustomerAuthenticationRequest;
import com.example.serviceApp.security.User.User;
import com.sun.net.httpserver.Authenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request){
        AuthenticationResponse response = authService.register(request);
        if (response == null) {
            return ResponseEntity.badRequest().body("USER EXIST"); // Return bad request if email is already taken
        }
        return ResponseEntity.ok(response); // Return ok response with AuthenticationResponse
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customer/authenticate")
    public ResponseEntity authenticateCustomer(@RequestBody CustomerAuthenticationRequest request){
        AuthenticationResponse response = authService.authenticateCustomer(request);
        log.info("Logged user:" +SecurityContextHolder.getContext().getAuthentication().getName());
        if (response == null) {
            return ResponseEntity.badRequest().body("Password expired"); //todo na razie zadrutowane tak zeby dostac odpowiedz zamiast 403
        }
        return ResponseEntity.ok(response);
    }
    @PostMapping("/password")
    public User changePassword(@RequestBody PasswordChangeRequest request){
//        if(authService.changePassword(request)){
//            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
//        }
//        return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        return authService.changePassword(request);
    }
    @PostMapping("/refresh")
    public ResponseEntity refreshAuthentication(@RequestBody TokenRequest request){
        return authService.refreshAuthentication(request);
    }
}
