package com.service.boardservice.service;

import com.service.boardservice.dto.AddNewMessageRequestDTO;
import com.service.boardservice.dto.BoardDTO;
import com.service.boardservice.dto.BoardMessageDTO;
import com.service.boardservice.dto.CreateBoardRequestDTO;
import com.service.boardservice.model.Board;
import com.service.boardservice.model.Message;
import com.service.boardservice.repository.BoardRepository;
import com.service.boardservice.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final MessageRepository messageRepository;

    public void createBoard(CreateBoardRequestDTO request) {
        if (request.getEventID() == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        Board board = new Board();
        log.error("1)-----Board object created----: {}", board);
        board.setEventID(request.getEventID());
        log.error("2)-----Board object created----: {}", board.toString());
        board.setTitle(request.getTitle());
        log.error("3)-----Board object created----: {}", board.toString());
        board.setDescription(request.getDescription());
        // log board object
        log.error("4)-----Board object created----: {}", board.toString());
        boardRepository.save(board);
    }

    /**
     * Method to get a board by event ID, list of message is logically
     * subdivided in page of page. each page has 20 messages.
     * fetch message filtered by page number and size, es. if page number is 1,
     * fetch messages from 0 to 20 (the last 20 messages written on the board
     */
    public BoardDTO getBoard(Long eventID, int pageNumber, int pageSize) {
        log.info("Fetching board for event ID: {}", eventID);
        Board board = boardRepository.findByEventID(eventID);
        if (board == null) {
            log.warn("Board not found for event ID: {}", eventID);
            return null;
        }

        // Fetch messages for the board with pagination and sorting by date
        var messages = messageRepository.findByBoard(board)
                .stream()
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate())) // Sort messages by date in descending order
                .skip((long) pageNumber * pageSize) // Apply pagination
                .limit(pageSize)
                .map(message -> new BoardMessageDTO(
                        message.getId(),
                        message.getContent(),
                        message.getUsername(),
                        message.getDate()
                    )
                )
                .toList();

        return BoardDTO.builder()
                .eventID(board.getEventID())
                .title(board.getTitle())
                .description(board.getDescription())
                .messages(messages)
                .build();
    }

    public Boolean isBoardExists(Long eventID) {
        log.info("Checking if board exists for event ID: {}", eventID);
        return boardRepository.findByEventID(eventID) != null;
    }

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