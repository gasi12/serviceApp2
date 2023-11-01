package com.example.serviceApp.chat;

import com.google.common.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@CrossOrigin
public class GreetingController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final Cache<Long, List<String>> userCache;
    @MessageMapping("/hello/{id}")
    public void orderGreeting(@DestinationVariable Long id, ChatMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        // Extract the username from the session attributes
        String userName = (String) headerAccessor.getSessionAttributes().getOrDefault("userName", "null");

        List<String> validUsers = userCache.getIfPresent(id);

        if (validUsers!=null) {


            if (validUsers.contains(message.getRecipient())&&validUsers.contains(userName)) {
                // The recipient is valid

                // Save chat message in database
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setServiceId(id);
                chatMessage.setAuthor(userName);
                chatMessage.setContent(message.getContent());
                chatMessage.setTimestamp(LocalDateTime.now());
                chatMessageRepository.save(chatMessage);

                simpMessagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/reply", chatMessage);
            } else {
                throw new Exception("You are not authorized to send messages to this user");
            }
        } else {
            throw new Exception("no such request");
        }
    }
}

