package com.example.serviceApp.customer;

import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class Customer extends UserImplementation {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;


//@JsonIgnore
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private String password;

    private Long phoneNumber;

    @Transient
    private String plainPassword;

//    @Enumerated(EnumType.STRING)
//    private Role role = Role.CUSTOMER; // They are always initialized with CUSTOMER role
@JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ServiceRequest> serviceRequestList;

 
    @Override
    public String getUsername() {
        return phoneNumber.toString();
    }


}