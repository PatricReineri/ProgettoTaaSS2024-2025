package com.service.guestgameservice.service;

import com.service.guestgameservice.dto.EventDeletionAckDTO;
import com.service.guestgameservice.repository.GameRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class DeleteGameMessageListener {
    @Value("${spring.rabbitmq.routing-key.delete-ack}")
    private String deleteAckRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final GameRepository gameRepository;

    public DeleteGameMessageListener(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-game}")
    @Transactional
    public void deleteGame(Long eventID) {
        try {
            EventDeletionAckDTO response = new EventDeletionAckDTO(eventID, "guest-game", true);
            gameRepository.deleteById(eventID);
            rabbitTemplate.convertAndSend(deleteAckRoutingKey, response);
        } catch (Exception e) {
            EventDeletionAckDTO response = new EventDeletionAckDTO(eventID, "guest-game", false);
            rabbitTemplate.convertAndSend(deleteAckRoutingKey, response);
        }
    }
}