package com.example.serviceApp.security.auth;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerDetailsService;
import com.example.serviceApp.customer.Dto.CustomerAuthenticationRequest;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.customExeptions.PasswordChangeRequiredException;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.security.auth.Dto.*;
import com.example.serviceApp.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
private final UserRepository UserRepository;
private final CustomerRepository customerRepository;
private final PasswordEncoder passwordEncoder;
private final JwtService jwtService;
private final AuthenticationManager authenticationManager;
private final CustomerDetailsService CustomerDetailsService;
    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> exist = UserRepository.findByEmail(request.getEmail());
        if (exist.isPresent()){
            return null; // Return null or throw an exception to indicate that the email is already taken
        }
        var user = User.userBuilder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        //user.setRole(Role.ADMIN);
        UserRepository.save(user);
        var jwtToken = jwtService.generateToken(user,"user");
        return AuthenticationResponse.builder().token(jwtToken).build(); // Return AuthenticationResponse with JWT token
    }


    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        log.info("email: " +request.getEmail()+", password: "+request.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        log.info("email: " +request.getEmail()+", password: "+request.getPassword());
        User user = UserRepository.findByEmail(request.getEmail()).orElseThrow(()-> new IllegalArgumentException("user not found"));

        if(user.isPasswordChangeRequired()){
            log.info("PASSWORD  CHANGE REQUERIEDF");
            throw new PasswordChangeRequiredException ("password change required");
        }
        String jwtToken = jwtService.generateToken(user,"user");
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse authenticateCustomer(CustomerAuthenticationRequest request) {
        log.info("Phone Number: " + request.getPhoneNumber());

        Customer customer = customerRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + request.getPhoneNumber()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getPhoneNumber(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid phone number or password");
        }

        var jwtToken = jwtService.generateToken(customer, "customer");
        var refreshToken = jwtService.generateRefreshToken(customer);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public ResponseEntity refreshAuthentication( TokenRequest request){
        String refreshToken = request.getToken();
        UserDetails userDetails = jwtService.getUserDetails(refreshToken);
        if(jwtService.isTokenValid(refreshToken,userDetails) && jwtService.isRefreshToken(refreshToken)) {

            String newJwtToken = jwtService.generateToken(userDetails,"user");
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            return ResponseEntity.ok(AuthenticationResponse.builder().token(newJwtToken).refreshToken(newRefreshToken).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

    public User changeUserPassword(PasswordChangeRequest request) {
        String email = request.getLogin();
        Optional<User> optionalUser = UserRepository.findByEmail(email);
log.info(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();

            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                user.setPasswordChangeRequired(false);
                return UserRepository.save(user);

            } else {
                throw new IllegalArgumentException("Invalid old password");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    public Customer changeCustomerPassword(PasswordChangeRequest request) {
        Long phoneNumber = Long.parseLong(request.getLogin());  // Assume phone number is string in request
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

}
