package com.example.serviceApp.security.auth.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationRequest {
    private String username;
    private String password;
}
