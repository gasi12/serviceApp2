package com.example.serviceApp.chat;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HelloMessage {
    private Long id;
    private String name;
    //private String author;
    public HelloMessage() {

}
}