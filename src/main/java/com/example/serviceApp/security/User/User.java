package com.example.serviceApp.security.User;


import com.example.serviceApp.UserImplementation;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User extends UserImplementation {
    private String email;
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