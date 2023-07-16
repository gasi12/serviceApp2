package com.example.serviceApp.chat;



import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.*;

@Slf4j
public class TicketHandshakeInterceptor implements HandshakeInterceptor {

    // A HashMap to store valid tickets
    private static List<String> validTickets = new ArrayList<>();

    // A method to generate a ticket and add it to validTickets
    public static String generateAndStoreTicket() {
        String ticket = UUID.randomUUID().toString();
        validTickets.add(ticket);
        return ticket;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,//todo wyslac token w pierwszej wiadomosci zamist w uri
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extract the ticket from the URL
        String uri = request.getURI().toString();
        String ticket = uri.substring(uri.indexOf("=") + 1);
        log.info("TICKET GOT AT INTERCEPTOR "+ticket);//todo wywalic te logi
        log.info("VALID TICKETS ARE " + validTickets.toString());
        // If the ticket is valid, remove it from validTickets and allow the handshake
        if (validTickets.contains(ticket)) {
            validTickets.remove(ticket);
            return true;
        } else {
            // If the ticket is invalid, reject the handshake
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
