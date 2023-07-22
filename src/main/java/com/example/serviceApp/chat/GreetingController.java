package com.example.serviceApp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class GreetingController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/hello/{id}")//todo podzielic sockety na userow
   // @SendTo("/topic/greetings")
    public void orderGreeting(@DestinationVariable Long id, HelloMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(1000); // simulated delay
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        String userName = (String) sessionAttributes.get("userName");
      simpMessagingTemplate.convertAndSend("/topic/greetings/" + id, new Greeting("Message: "+message.getName()+"id: "+ id+" " +userName));


    }

}