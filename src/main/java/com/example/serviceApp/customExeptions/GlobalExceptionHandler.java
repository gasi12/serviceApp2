package com.example.serviceApp.customExeptions;

import com.example.serviceApp.customExeptions.NoContentException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleUnauthorized(HttpClientErrorException.Unauthorized e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorMessage.setError("auth error");
        errorMessage.setMessage(e.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.NOT_FOUND.value());
        errorMessage.setError("Not Found");
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
