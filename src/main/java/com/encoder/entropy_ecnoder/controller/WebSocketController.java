package com.encoder.entropy_ecnoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/topic")
    @SendTo("/topic/replay")
    public String processMessageFromClient(@Payload String message, SimpMessageHeaderAccessor  headerAccessor) {
        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        headerAccessor.setSessionId(sessionId);
        // handle messages ..make service
        messagingTemplate.convertAndSend("/topic/reply","" );
        return message;
    }

}