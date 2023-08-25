package com.example.serviceApp.customExeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

public class PasswordChangeRequiredException extends RuntimeException {

    public PasswordChangeRequiredException(String message) {
        super(message);
    }

    public PasswordChangeRequiredException() {

    }
}
