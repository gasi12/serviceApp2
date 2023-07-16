package com.example.serviceApp.chat;

import com.example.serviceApp.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class GreetingController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/hello/{id}")//todo podzielic sockety na userow
   // @SendTo("/topic/greetings")
    public void orderGreeting(@DestinationVariable Long id, HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
      simpMessagingTemplate.convertAndSend("/topic/greetings/" + id, new Greeting("Message: "+message.getName()+"id: "+ id));
       // return new Greeting(  ", Message: " + HtmlUtils.htmlEscape(message.getName())+ "id "+id );

    }

}