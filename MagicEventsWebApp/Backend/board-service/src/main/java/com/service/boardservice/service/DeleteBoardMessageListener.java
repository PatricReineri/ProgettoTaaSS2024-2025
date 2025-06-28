package com.service.boardservice.service;

import com.service.boardservice.dto.EventDeletionAckDTO;
import com.service.boardservice.repository.BoardRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class DeleteBoardMessageListener {
    @Value("${spring.rabbitmq.routing-key.delete-ack}")
    private String deleteAckRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final BoardRepository boardRepository;

    public DeleteBoardMessageListener(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-board}")
    @Transactional
    public void deleteBoard(Long eventID) {
        try {
            EventDeletionAckDTO response = new EventDeletionAckDTO(eventID, "board", true);
            boardRepository.deleteByEventID(eventID);
            rabbitTemplate.convertAndSend(deleteAckRoutingKey, response);
        } catch (Exception e) {
            EventDeletionAckDTO response = new EventDeletionAckDTO(eventID, "board", false);
            rabbitTemplate.convertAndSend(deleteAckRoutingKey, response);
        }
    }
}
