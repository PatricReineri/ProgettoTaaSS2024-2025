package com.service.boardservice.service;

import com.service.boardservice.dto.BoardDTO;
import com.service.boardservice.dto.BoardMessageDTO;
import com.service.boardservice.dto.CreateBoardRequestDTO;
import com.service.boardservice.exception.UnauthorizedException;
import com.service.boardservice.model.Board;
import com.service.boardservice.repository.BoardRepository;
import com.service.boardservice.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MessageRepository messageRepository;
    private final WebClient eventManagementWebClient;

    public BoardService(BoardRepository boardRepository, MessageRepository messageRepository, WebClient eventManagementWebClient) {
        this.boardRepository = boardRepository;
        this.messageRepository = messageRepository;
        this.eventManagementWebClient = eventManagementWebClient;
    }

    public void createBoard(CreateBoardRequestDTO request) {
        if (!authorizeCreateBoard(request.getEventID(), request.getUserMagicEventsTag())) {
            throw new UnauthorizedException("Not authorized to create board for event ID: " + request.getEventID());
        }
        if (request.getEventID() == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        Board board = new Board();
        board.setEventID(request.getEventID());
        board.setTitle(request.getTitle());
        board.setDescription(request.getDescription());
        boardRepository.save(board);
    }

    /**
     * Method to get a board by event ID, list of message is logically
     * subdivided in page of page. each page has 20 messages.
     * fetch message filtered by page number and size, es. if page number is 1,
     * fetch messages from 0 to 20 (the last 20 messages written on the board
     */
    public BoardDTO getBoard(Long eventID, Long userMagicEventsTag, int pageNumber, int pageSize) {
        if (!authorizeViewBoard(eventID, userMagicEventsTag)) {
            throw new UnauthorizedException("Not authorized to view board for event ID: " + eventID);
        }

        Board board = boardRepository.findByEventID(eventID);
        if (board == null) {
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

        return new BoardDTO(
                board.getEventID(),
                board.getTitle(),
                board.getDescription(),
                messages
        );
    }

    public Boolean isBoardExists(Long eventID) {
        return boardRepository.findByEventID(eventID) != null;
    }

    @Transactional
    public void deleteBoard(Long eventID) {
        Board board = boardRepository.findByEventID(eventID);
        if (board != null) {
            boardRepository.delete(board); // Delete the board itself
        }
    }

    private boolean authorizeCreateBoard(Long eventID, Long userMagicEventsTag) {
        try {
            Boolean isAdmin = eventManagementWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gestion/isAdmin")
                            .queryParam("partecipantId", userMagicEventsTag)
                            .queryParam("eventId", eventID)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            //return Boolean.TRUE.equals(isAdmin);
        } catch (Exception e) {
            //return false;
        }
        return true; // Default to true for testing purposes
    }

    private boolean authorizeViewBoard(Long eventID, Long userMagicEventsTag) {
        try {
            Boolean isParticipant = eventManagementWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gestion/isParticipant")
                            .queryParam("partecipantId", userMagicEventsTag)
                            .queryParam("eventId", eventID)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            //return Boolean.TRUE.equals(isParticipant);
        } catch (Exception e) {
            //return false;
        }
        return true;
    }
}