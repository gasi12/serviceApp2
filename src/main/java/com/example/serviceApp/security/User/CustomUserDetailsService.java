package com.example.serviceApp.security.User;


import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository; // Add this

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElse(null);

        if (user != null) {
            if (user.isPasswordChangeRequired()) {
                throw new IllegalArgumentException("change password");//todo exception change
            }
            return user;
        }

        // If user is not found, check for customer
        Customer customer = customerRepository.findByPhoneNumber(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return customer;
    }
}
