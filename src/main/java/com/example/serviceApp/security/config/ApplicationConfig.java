package com.example.serviceApp.security.config;

import com.example.serviceApp.UserDetailsServiceImplementation;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

private final UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsServiceImplementation);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String firstname=  "default";
            String lastname = "admin";
            String email = "default@admin";
            String password = "admin";
            Role role = Role.ADMIN;

            // Check if the default admin user already exists in the database
            if (!userRepository.findByEmail(email).isPresent()) {
                User admin = new User();
               admin.setFirstName(firstname);
               admin.setLastName(lastname);
               admin.setEmail(email);
               admin.setRole(role);
               admin.setPassword(passwordEncoder.encode(password));
               admin.setPasswordChangeRequired(true);
               admin.setIsPhoneNumberVerified(true);


                userRepository.save(admin);
            }
        };
    }
}
