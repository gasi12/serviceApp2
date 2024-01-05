package com.example.serviceApp.customer;

import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.device.Device;
import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Table
@Entity

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class Customer extends UserImplementation {


    private Long phoneNumber;


    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Device> devices;



 
    @Override
    public String getUsername() {
        return phoneNumber.toString();
    }

    public Customer(String firstname, String lastname, Long phoneNumber) {
        super(firstname, lastname);
        this.phoneNumber = phoneNumber;
    }
}