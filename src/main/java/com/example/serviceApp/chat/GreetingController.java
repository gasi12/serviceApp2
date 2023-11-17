package com.example.serviceApp.chat;

import com.google.common.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin
public class GreetingController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final Cache<Long, List<String>> userCache;
//    @MessageMapping("/hello/{id}")
//    public void orderGreeting(@DestinationVariable Long id,Principal u, ChatMessage message, SimpMessageHeaderAccessor headerAccessor,@Header("simpSessionId") String sessionId) throws Exception {
//        // Extract the username from the session attributes
//        String userName = (String) headerAccessor.getSessionAttributes().getOrDefault("userName", "null");
//
//        log.info(u.getName());
//
//        List<String> validUsers = userCache.getIfPresent(id);
//
//        if (validUsers!=null) {
//            if (validUsers.contains(message.getRecipient())&&validUsers.contains(userName)) {
//                // The recipient is valid
//
//                // Save chat message in database
//                ChatMessage chatMessage = new ChatMessage();
//                chatMessage.setServiceId(id);
//                chatMessage.setAuthor(userName);
//                chatMessage.setContent(message.getContent().toUpperCase());
//                chatMessage.setTimestamp(LocalDateTime.now());
//                chatMessage.setRecipient(message.getRecipient());
//                log.info(message.getRecipient());
//                log.info(chatMessage.toString());
//                chatMessageRepository.save(chatMessage);
//
//                simpMessagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/greetings", chatMessage);
//            } else {
//                throw new Exception("You are not authorized to send messages to this user");
//            }
//        } else {
//            throw new Exception("no such request");
//        }
//    }

//@MessageMapping("/greetings")
//@SendToUser("/queue/greetings")
//public String reply(@Payload String message,
//                    Principal user) {
//    log.info("principal name "+ user.getName());
//    return  "Hello " + message;
//}

    @MessageMapping("/greetings")
    public void reply(@Payload String message, Principal user) {
        log.info("principal name "+ user.getName());
        String response = "Hello " + message;
        simpMessagingTemplate.convertAndSendToUser("efault@admin", "/queue/greetings", response);
    }
}

