package com.example.serviceApp.customer.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CustomerAuthenticationRequest {
    private Long phoneNumber;
    private String password;
}