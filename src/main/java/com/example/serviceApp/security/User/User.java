package com.example.serviceApp.security.User;


import com.example.serviceApp.UserImplementation;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User extends UserImplementation {
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ServiceRequest> serviceRequestList;
    @Override
    public String getUsername() {
        return email;
    }

    @Builder(builderMethodName = "userBuilder")
    public User(String firstname, String lastname, String password, Role role, String email) {
        super(firstname, lastname, password, role);
        this.email = email;

    }

}