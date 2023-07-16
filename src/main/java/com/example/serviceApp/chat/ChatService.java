package com.example.serviceApp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSpecificMessage(ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(
                "/app/" + chatMessage.getTopic(),
                chatMessage);
    }
}