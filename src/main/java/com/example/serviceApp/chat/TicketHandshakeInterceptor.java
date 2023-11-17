package com.example.serviceApp.chat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@RequiredArgsConstructor
@Slf4j
public class TicketHandshakeInterceptor implements HandshakeInterceptor {


private final TicketService ticketService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extract the ticket from the URL
        String uri = request.getURI().toString();
        String ticket = uri.substring(uri.indexOf("=") + 1);
        log.info("TICKET GOT AT INTERCEPTOR "+ticket);
       // log.info("VALID TICKETS ARE " + tickets.toString());
        return ticketService.validateTicket(ticket,attributes);

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
