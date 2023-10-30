package com.example.serviceApp.security.User;


import com.example.serviceApp.customExeptions.PasswordChangeRequiredException;
import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    //private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // Optional<User> user
        log.info("CustomUserDetailsService username searching  for: "+username);
               return userRepository.findByEmail(username).orElseThrow();
//        if(user.isPresent()){
//            return user.get();
//        }
//        else {//todo drutologia, to trzeba byloby rozbic na dwa osobne authprovidery w security config
//          return   customerRepository.findByPhoneNumber(Long.parseLong(username)).orElseThrow();
//        }
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

    }
}
