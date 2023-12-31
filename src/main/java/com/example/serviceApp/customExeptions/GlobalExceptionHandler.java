package com.example.serviceApp.customExeptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordChangeRequiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessage handlePasswordChangeRequired(PasswordChangeRequiredException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorMessage.setError("password not set");
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessage handleAuthenticationException(AuthenticationException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorMessage.setError("auth error");
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }


    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessage handleUnauthorized(HttpClientErrorException.Unauthorized e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorMessage.setError("auth error");
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.NOT_FOUND.value());
        errorMessage.setError("Not Found");
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }
    @ExceptionHandler(BadStatusException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadStatusException(BadStatusException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.NOT_FOUND.value());
        errorMessage.setError("Wrong status");
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorMessage {
        private int status;
        private String error;
        private String message;
        // getters and setters
    }
}
