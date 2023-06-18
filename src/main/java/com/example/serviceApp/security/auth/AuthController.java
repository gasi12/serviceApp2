package com.example.serviceApp.security.auth;

import com.example.serviceApp.security.User.User;
import com.sun.net.httpserver.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
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
        AuthenticationResponse response = authService.authenticate(request);
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
}
