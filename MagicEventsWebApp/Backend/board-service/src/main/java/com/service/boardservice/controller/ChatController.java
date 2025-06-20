package com.service.boardservice.controller;

import com.service.boardservice.dto.AddNewMessageRequestDTO;
import com.service.boardservice.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("chat/sendMessage/{eventID}")
    @SendTo("/topic/chat/{eventID}")
    public AddNewMessageRequestDTO receiveMessage(@Payload AddNewMessageRequestDTO message) {
        chatService.addNewMessage(message);
        return message;
    }
}
