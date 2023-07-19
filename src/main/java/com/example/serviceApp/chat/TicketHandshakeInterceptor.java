package com.example.serviceApp.chat;



import com.example.serviceApp.customer.CustomerRepository;
import com.example.serviceApp.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.*;

@Slf4j
public class TicketHandshakeInterceptor implements HandshakeInterceptor {
    // A HashMap to store valid tickets
    private static HashMap<String,String> tickets = new HashMap<>();

    // A method to generate a ticket and add it to validTickets
    public static String generateAndStoreTicket() {
        String ticket = UUID.randomUUID().toString();
        String ticketOwner = SecurityContextHolder.getContext().getAuthentication().getName();
        tickets.put(ticket,ticketOwner);//todo na pewno nie odwrotnie?

        return ticket;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,//todo wyslac token w pierwszej wiadomosci zamist w uri
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extract the ticket from the URL
        String uri = request.getURI().toString();
        String ticket = uri.substring(uri.indexOf("=") + 1);
        log.info("TICKET GOT AT INTERCEPTOR "+ticket);//todo wywalic te logi
        log.info("VALID TICKETS ARE " + tickets.toString());//todo zmienic z listy na hashmape
        // If the ticket is valid, remove it from validTickets and allow the handshake
        if (tickets.containsKey(ticket)&&tickets.get(ticket)
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            log.info("ticket valid");//todo wywalic te logi
            tickets.remove(ticket);
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
