package com.example.serviceApp.customer;

import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Table
@Entity

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Customer extends UserImplementation {


    private Long phoneNumber;

    @Transient
    private String plainPassword;

@JsonManagedReference
@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ServiceRequest> serviceRequestList;

 
    @Override
    public String getUsername() {
        return phoneNumber.toString();
    }
    @Override
    public String toString() {
        return "Customer{" +
                "phoneNumber=" + phoneNumber +
                ", serviceRequestList=" + serviceRequestList.size() +
                '}';
    }


}