package com.example.serviceApp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class GreetingController {
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    @MessageMapping("/hello/{id}")//todo po co testowac skoro wydaje ci sie ze powinno dzialac
    public void orderGreeting(@DestinationVariable Long id, ChatMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(1000); // simulated delay
        ChatMessage chatMessage = new ChatMessage();
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        String userName = (String) sessionAttributes.getOrDefault("userName","null");


        // save chat message in database

        chatMessage.setServiceId(id);
        chatMessage.setAuthor(userName);
        chatMessage.setContent(message.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/greetings/" + id,chatMessage );
    }

}