package com.example.serviceApp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final List<UserDetailsService> services;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetailsService service : services) {
            log.info("services are "+service.getClass().getSimpleName());
        }

        for (UserDetailsService service : services) {
        log.info("now we are in "+service.getClass().getSimpleName());
            try {
                log.info("trying to load ");
                return service.loadUserByUsername(username);
            } catch (UsernameNotFoundException ignored) {
                log.info("catch exception in service "+service.getClass().getSimpleName());
            }
        }
        log.info("nie znalazlo");
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}