package com.service.boardservice.service;

import com.service.boardservice.repository.BoardRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteBoardMessageListener {

    private final BoardRepository boardRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-board}")
    @Transactional
    public void deleteBoard(Long eventID) {
        log.info("Received delete board message for eventID: {}", eventID);
        try {
            boardRepository.deleteByEventID(eventID);
            log.info("Board and his message with eventID {} deleted successfully", eventID);
        } catch (Exception e) {
            log.error("Error deleting board with eventID {}: {}", eventID, e.getMessage());
        }
    }
}
