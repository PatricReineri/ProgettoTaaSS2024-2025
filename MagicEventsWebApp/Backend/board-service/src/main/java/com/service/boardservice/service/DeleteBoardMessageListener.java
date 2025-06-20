package com.service.boardservice.service;

import com.service.boardservice.repository.BoardRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class DeleteBoardMessageListener {
    private final BoardRepository boardRepository;

    public DeleteBoardMessageListener(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-board}")
    @Transactional
    public void deleteBoard(Long eventID) {
        try {
            boardRepository.deleteByEventID(eventID);
        } catch (Exception e) {
        }
    }
}
