package com.service.guestgameservice.service;

import com.service.guestgameservice.repository.GameRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteGameMessageListener {
    private final GameRepository gameRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-game}")
    @Transactional
    public void deleteGame(Long eventId) {
        log.info("Received delete game message for eventId: {}", eventId);
        try {
            gameRepository.deleteById(eventId);
            log.info("Game with eventId {} deleted successfully", eventId);
        } catch (Exception e) {
            log.error("Error deleting game with eventId {}: {}", eventId, e.getMessage());
        }
    }
}