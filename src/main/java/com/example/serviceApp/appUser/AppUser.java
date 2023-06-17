package com.example.serviceApp.appUser;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String userName;

    private Long phoneNumber;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.PERSIST)
    private List<ServiceRequest> serviceRequestList;

    public AppUser(String userName, Long phoneNumber, List<ServiceRequest> serviceRequestList) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.serviceRequestList = serviceRequestList;
    }

}
