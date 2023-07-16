//package com.example.serviceApp.chat;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//
//public class ChatController {
//    @Autowired
//    private final ChatService service;
//
//    public ChatController(ChatService service) {
//        this.service = service;
//    }
//
//    @MessageMapping("/{topic}")
//
//    public void processMessage(@Payload ChatMessage chatMessage, @DestinationVariable String topic) {
//        chatMessage.setTopic(topic);
//        service.sendSpecificMessage(chatMessage);
//    }
//}