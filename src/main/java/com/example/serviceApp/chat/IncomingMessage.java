package com.example.serviceApp.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomingMessage {

    private String message;
    private String author;

    @Override
    public String toString() {
        return "ChatMessage{" +
                ", content='" + message + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}