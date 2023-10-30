//package com.example.serviceApp.security.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//@Slf4j
//@Service
//public class TicketService {
//    private final ConcurrentHashMap<String, Boolean> tickets = new ConcurrentHashMap<>();
//
//    public String createTicket() {
//        String ticket = UUID.randomUUID().toString();
//        tickets.put(ticket, true); // The ticket is valid once created
//      log.info("Created ticket "+ticket);
//        return ticket;
//    }
//
//    public boolean validateAndInvalidateTicket(String ticket) {
//        return tickets.remove(ticket) != null;  // The ticket can only be used once
//    }
//}
