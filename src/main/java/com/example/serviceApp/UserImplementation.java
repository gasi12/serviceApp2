package com.example.serviceApp;

import com.example.serviceApp.security.User.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@Data
@NoArgsConstructor
@MappedSuperclass

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class UserImplementation implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;
    private Boolean passwordChangeRequired = false;//todo pamietac to na koncu dac na true
    private Boolean isPhoneNumberVerified = false;

    public UserImplementation(String firstname, String lastname, String password, Role role) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.password = password;
        this.role = role;
    }

    public UserImplementation(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean isPasswordChangeRequired(){
        return passwordChangeRequired;
    }
    public abstract String getUsername();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role != null) {
            return List.of(new SimpleGrantedAuthority(role.name()));
        } else {
            return List.of();
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
