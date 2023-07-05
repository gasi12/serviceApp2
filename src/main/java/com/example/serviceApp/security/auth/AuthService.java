package com.example.serviceApp.security.auth;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerAuthenticationRequest;
import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
private final UserRepository repository;
private final CustomerRepository customerRepository;
private final PasswordEncoder passwordEncoder;
private final JwtService jwtService;
private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> exist = repository.findByEmail(request.getEmail());
        if (exist.isPresent()){
            return null; // Return null or throw an exception to indicate that the email is already taken
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        user.setRole(Role.ADMIN);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build(); // Return AuthenticationResponse with JWT token
    }


    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        if(user.isPasswordChangeRequired()){
            return null;
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse authenticateCustomer(CustomerAuthenticationRequest request) {

        Customer customer = customerRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(customer);
        var refreshToken = jwtService.generateRefreshToken(customer);

        return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken).build();
    }
    public ResponseEntity refreshAuthentication( TokenRequest request){
        String refreshToken = request.getToken();
        UserDetails userDetails = jwtService.getUserDetails(refreshToken);
        if(jwtService.isTokenValid(refreshToken,userDetails) && jwtService.isRefreshToken(refreshToken)) {

            String newJwtToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            return ResponseEntity.ok(AuthenticationResponse.builder().token(newJwtToken).refreshToken(newRefreshToken).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

    public User changePassword(PasswordChangeRequest request) {
        String email = request.getEmail();
        Optional<User> optionalUser = repository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();

            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                user.setPasswordChangeRequired(false);
                return repository.save(user);

            } else {
                throw new IllegalArgumentException("Invalid old password");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
