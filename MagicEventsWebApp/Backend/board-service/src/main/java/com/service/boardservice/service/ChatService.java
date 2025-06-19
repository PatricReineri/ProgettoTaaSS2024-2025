package com.service.boardservice.service;

import com.service.boardservice.dto.AddNewMessageRequestDTO;
import com.service.boardservice.model.Board;
import com.service.boardservice.model.Message;
import com.service.boardservice.repository.BoardRepository;
import com.service.boardservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final BoardRepository boardRepository;
    private final MessageRepository messageRepository;

    public ChatService(BoardRepository boardRepository, MessageRepository messageRepository) {
        this.boardRepository = boardRepository;
        this.messageRepository = messageRepository;
    }

    public void addNewMessage(AddNewMessageRequestDTO request) {
        Board board = boardRepository.findByEventID(request.getEventID());
        if (board == null) {
            throw new IllegalArgumentException("Board not found for event ID: " + request.getEventID());
        }

        Message message = new Message(
                request.getContent(),
                request.getUsername(),
                request.getDateTime(),
                board
        );
        
        messageRepository.save(message);
    }
}
