package com.example.serviceApp.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Service
public class TicketService {
    private static final HashMap<String,String> tickets = new HashMap<>();

    public static String generateAndStoreTicket() {
        String ticket = UUID.randomUUID().toString();
        String ticketOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        tickets.put(ticket,ticketOwner);
        log.info("put ticket"+ ticket + " to " + ticketOwner);
        return ticket;
    }

    public boolean validateTicket(String ticket, Map<String, Object> attributes){
        // If the ticket is valid, remove it from validTickets and allow the handshake
        if (tickets.containsKey(ticket)) {
            log.info("ticket valid");
            attributes.put("userName", tickets.get(ticket));
            tickets.remove(ticket);
            return true;
        } else {
            log.info("ticket: "+ ticket+ " is invalid");
            return false;
        }
    }
}
