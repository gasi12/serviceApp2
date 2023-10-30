//package com.example.serviceApp.chat;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Controller
//@RequiredArgsConstructor
//@CrossOrigin
//public class GreetingController {
//
//    private final SimpMessagingTemplate simpMessagingTemplate;
//    private final ChatMessageRepository chatMessageRepository;
//    private final ConcurrentHashMap<String, HashMap<Long, String>> userOrdersCache;
//
//    @MessageMapping("/hello/{id}")
//    public void orderGreeting(@DestinationVariable Long id, ChatMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        // Extract the username from the session attributes
//        String userName = (String) headerAccessor.getSessionAttributes().getOrDefault("userName", "null");
//
//        // Get the map of valid orders and recipients for this user
//        Map<Long, String> ordersRecipientsMap = userOrdersCache.getOrDefault(userName, new HashMap<>());//todo dodać usera and employee do cachu
//
//        // Check if the order ID exists in the user's map of valid orders
//        if (ordersRecipientsMap.containsKey(id)) {
//            // The user is authorized to send messages
//
//            // Check if the recipient is valid
//            String validRecipient = ordersRecipientsMap.get(id);
//            if (message.getRecipient().equals(validRecipient)) {
//                // The recipient is valid
//
//                // Save chat message in database
//                ChatMessage chatMessage = new ChatMessage();
//                chatMessage.setServiceId(id);
//                chatMessage.setAuthor(userName);
//                chatMessage.setContent(message.getContent());
//                chatMessage.setTimestamp(LocalDateTime.now());
//                chatMessageRepository.save(chatMessage);
//
//                simpMessagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/reply", chatMessage);
//            } else {
//                throw new Exception("You are not authorized to send messages to this user");
//            }
//        } else {
//            throw new Exception("You are not authorized to send messages in this chat");
//        }
//    }
//}
//
