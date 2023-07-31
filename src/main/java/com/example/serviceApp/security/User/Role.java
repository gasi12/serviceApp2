package com.example.serviceApp.security.User;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;




@RequiredArgsConstructor
public enum Role {

    USER,
    CUSTOMER, ADMIN
}