package com.example.serviceApp.security.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationRequest {
    private String email;
    private String password;
}
