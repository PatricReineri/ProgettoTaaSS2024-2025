package com.service.boardservice.controller;

import com.service.boardservice.dto.AddNewMessageRequestDTO;
import com.service.boardservice.dto.DeleteMessageRequestDTO;
import com.service.boardservice.exception.UnauthorizedException;
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
        try {
            chatService.addNewMessage(message);
            return message;
        } catch (UnauthorizedException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @MessageMapping("chat/deleteMessage/{eventID}")
    @SendTo("/topic/chat/deleteMessage/{eventID}")
    public DeleteMessageRequestDTO deleteMessage(@Payload DeleteMessageRequestDTO request) {
        try {
            return chatService.deleteMessage(request);
        } catch (UnauthorizedException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
