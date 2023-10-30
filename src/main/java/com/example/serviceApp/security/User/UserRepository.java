package com.example.serviceApp.security.User;

import com.example.serviceApp.UserImplementation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);





}
