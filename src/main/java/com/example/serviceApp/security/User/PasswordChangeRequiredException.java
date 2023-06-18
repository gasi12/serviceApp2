package com.example.serviceApp.security.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

public class PasswordChangeRequiredException extends AuthenticationException {

    public PasswordChangeRequiredException(String message) {
        super(message);
    }

    // ...
}
