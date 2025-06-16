package com.service.boardservice.service;

import com.service.boardservice.dto.AddNewMessageRequestDTO;
import com.service.boardservice.model.Board;
import com.service.boardservice.model.Message;
import com.service.boardservice.repository.BoardRepository;
import com.service.boardservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final BoardRepository boardRepository;
    private final MessageRepository messageRepository;

    public void addNewMessage(AddNewMessageRequestDTO request){
        log.info("Adding new message to board for event ID: {}", request.getEventID());
        Board board = boardRepository.findByEventID(request.getEventID());

        if (board == null) {
            log.warn("Board not found for event ID, can't add new Message: {}", request.getEventID());
            return;
        }

        // Create a new message and associate it with the board
        Message message = new Message();
        message.setContent(request.getContent());
        message.setUsername(request.getUsername());
        message.setDate(request.getDateTime());
        message.setBoard(board);
        messageRepository.save(message);
        log.info("Message added successfully to board for event ID: {}", request.getEventID());
    }
}
