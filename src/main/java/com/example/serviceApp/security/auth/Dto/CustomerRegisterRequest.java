package com.example.serviceApp.security.auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
