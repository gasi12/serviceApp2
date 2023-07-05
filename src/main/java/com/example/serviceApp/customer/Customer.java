package com.example.serviceApp.customer;

import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String password; // The password is set upon customer creation as mentioned

    private Long phoneNumber;

    @Transient
    private String plainPassword;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER; // They are always initialized with CUSTOMER role

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ServiceRequest> serviceRequestList;

    public Customer(String customerName, Long phoneNumber, List<ServiceRequest> serviceRequestList, String password) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.serviceRequestList = serviceRequestList;
        this.password = password; // set password
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber.toString(); // customer's username
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