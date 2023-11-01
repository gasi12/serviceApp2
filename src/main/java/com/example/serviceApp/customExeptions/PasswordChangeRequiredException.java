package com.example.serviceApp.customExeptions;

public class PasswordChangeRequiredException extends RuntimeException {

    public PasswordChangeRequiredException(String message) {
        super(message);
    }

    public PasswordChangeRequiredException() {

    }
}
