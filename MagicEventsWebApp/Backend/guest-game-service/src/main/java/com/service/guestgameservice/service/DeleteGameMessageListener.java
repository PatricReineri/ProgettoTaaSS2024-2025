package com.service.guestgameservice.service;

import com.service.guestgameservice.repository.GameRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class DeleteGameMessageListener {
    private final GameRepository gameRepository;

    public DeleteGameMessageListener(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-game}")
    @Transactional
    public void deleteGame(Long eventId) {
        try {
            gameRepository.deleteById(eventId);
        } catch (Exception e) {
            // Handle error silently or throw RuntimeException if needed
        }
    }
}