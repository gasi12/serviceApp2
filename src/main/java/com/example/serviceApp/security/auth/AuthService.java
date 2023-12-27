package com.example.serviceApp.security.auth;

import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.customExeptions.PasswordChangeRequiredException;
import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.security.auth.Dto.*;
import com.example.serviceApp.security.config.JwtService;

import com.example.serviceApp.security.twilio.TwilioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
private final UserRepository userRepository;
private final CustomerRepository customerRepository;
private final PasswordEncoder passwordEncoder;
private final JwtService jwtService;
private final AuthenticationManager authenticationManager;
private final TwilioService twilioService;

    public AuthenticationResponse register(CustomerRegisterRequest request) {
        Optional<User> exist = userRepository.findByEmail(request.getEmail());
        if (exist.isPresent()){
            throw new IllegalArgumentException("user exist"); // Return null or throw an exception to indicate that the email is already taken
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setIsPhoneNumberVerified(true);

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user,"user");
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        log.info("email: " +request.getUsername()+", password: "+request.getPassword());


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getUsername()).orElseThrow(()-> new IllegalArgumentException("user not found"));

        if(user.isPasswordChangeRequired()){
            log.info("PASSWORD  CHANGE REQUERIED");
            throw new PasswordChangeRequiredException ("password change required");
        }
        String jwtToken = jwtService.generateToken(user,"user");
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


public AuthenticationResponse authenticate(AuthenticationRequest request, Function<String, Optional<? extends UserImplementation>> finder) {
    log.info("Username: " + request.getUsername());

    UserImplementation entity = finder.apply(request.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Entity not found with username: " + request.getUsername()));
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
    } catch (AuthenticationException e) {
        throw new BadCredentialsException("Invalid username or password");
    }

    var jwtToken = jwtService.generateToken(entity,entity.getClass().getSimpleName());
    var refreshToken = jwtService.generateRefreshToken(entity);

    return AuthenticationResponse.builder()
            .token(jwtToken)
            .refreshToken(refreshToken)
            .username(entity.getUsername())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .build();
}
    public AuthenticationResponse refreshAuthentication( RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        UserDetails userDetails = jwtService.getUserDetails(refreshToken);
        if(jwtService.isTokenValid(refreshToken,userDetails) && jwtService.isRefreshToken(refreshToken)) {

            // Cast UserDetails to your implementation class
            UserImplementation entity = (UserImplementation) userDetails;

            String newJwtToken = jwtService.generateToken(userDetails,"user");
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            return AuthenticationResponse.builder()
                    .token(newJwtToken)
                    .refreshToken(newRefreshToken)
                    .username(entity.getUsername())
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .build();
        } else {
            throw new IllegalArgumentException("token expired");
        }
    }

    public User changeUserPassword(PasswordChangeRequest request) {
        String email = request.getUsername();
        Optional<User> optionalUser = userRepository.findByEmail(email);
log.info(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();

            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                user.setPasswordChangeRequired(false);
                return userRepository.save(user);

            } else {
                throw new IllegalArgumentException("Invalid old password");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    public Customer changeCustomerPassword(PasswordChangeRequest request) {
        Long phoneNumber = Long.parseLong(request.getUsername());  // Assume phone number is string in request
        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(phoneNumber);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();

            if (passwordEncoder.matches(oldPassword, customer.getPassword())) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                customer.setPassword(encodedPassword);
                return customerRepository.save(customer);

            } else {
                throw new IllegalArgumentException("Invalid old password");
            }
        } else {
            throw new IllegalArgumentException("Customer not found");
        }
    }

    private void sendToken(String phoneNumber){
        log.info("Token sent to "+ phoneNumber);
    }
    public Boolean validateToken(AuthenticationRequest request) throws IOException, InterruptedException{
        Customer customer = customerRepository.findByPhoneNumber(Long.parseLong(request.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (twilioService.validateToken(request.getUsername(),request.getPassword())) {
            customer.setIsPhoneNumberVerified(true);
            log.info("Customer "+customer.getUsername() +" validated number successfully");
            return true;
        } else {
            return false;
        }
    }
    public Boolean registerCustomer(AuthenticationRequest request)throws IOException, InterruptedException{
        Long phoneNumber = Long.parseLong(request.getUsername());
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if(!customer.getIsPhoneNumberVerified()) {
            twilioService.sendToken(request.getUsername());
            throw new IllegalArgumentException("Phone number not verified");
        } else {
            return true;
        }
    }

}
