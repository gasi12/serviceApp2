
package com.example.serviceApp.chat;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final TicketService ticketService;

    public WebSocketConfig(TicketService ticketService) {
        this.ticketService = ticketService;

    }



    @Override
    public void registerStompEndpoints(StompEndpointRegistry
                                               registry) {
        registry.addEndpoint("/mywebsockets")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//                        // Extract the ticket from the URL
//                        String uri = request.getURI().toString();
//                        String ticket = uri.substring(uri.indexOf("=") + 1);
//
//                        // Validate the ticket and get the username
//                        if (ticketService.validateTicket(ticket, attributes)) {
//                            String userName = (String) attributes.get("userName");
//
//                            // Return a Principal with the username
//                            return new Principal() {
//                                @Override
//                                public String getName() {
//                                    return userName;
//                                }
//                            };
//                        }
//                        else {
//                            return null;
//                        }
                        return new Principal() {
                            @Override
                            public String getName() {
                                return "default@admin";
                            }
                        };
                    }
                })
                .setAllowedOrigins("http://localhost:3000")
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }





//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/gs-guide-websocket").setHandshakeHandler(new DefaultHandshakeHandler() {
//                    @Override
//                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//                        return new Principal() {
//                            @Override
//                            public String getName() {
//                                return "default@admin";
//                            }
//                        };
//                    }
//                } ).setAllowedOrigins("http://localhost:3000")
//                .setAllowedOriginPatterns("*").withSockJS().setInterceptors(new TicketHandshakeInterceptor(ticketService));
//    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/user");
//    }

//    @Override
//    public void configureClientOutboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//                // log the message details
//                log.info("outboundCommand: " + accessor.getCommand());
//                log.info("outboundDestination: " + accessor.getDestination());
//                log.info("outboundPayload: " + message.getPayload().toString());
//                return message;
//            }
//        });
//    }
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//                // log the message details
//                log.info("inboundCommand: " + accessor.getCommand());
//                log.info("inboundDestination: " + accessor.getDestination());
//                log.info("inboundPayload: " + new String((byte[]) message.getPayload()));
//                return message;
//            }
//        });
//    }


    }
