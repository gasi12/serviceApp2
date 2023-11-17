package com.example.serviceApp.customer;

import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

public class Customer extends UserImplementation {


    private Long phoneNumber;

    @Transient
    private String plainPassword;

@JsonManagedReference
@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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