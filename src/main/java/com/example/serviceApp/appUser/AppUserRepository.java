package com.example.serviceApp.appUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    void getUserById(Long id);

    boolean existsByPhoneNumber(Long phoneNumber);
//    Optional<AppUser> getAppUserByPhoneNumber(Long phoneNumber);
Optional<AppUser> getAppUserByPhoneNumber(Long phoneNumber);
    Optional<AppUser> getAppUserById(Long phoneNumber);

    Optional<AppUser> findByPhoneNumber(Long phoneNumber);

}
