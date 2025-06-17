package com.service.dummysender.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DeleteGuestGameSenderService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routingkey.delete-guestgame}")
    private String deleteGuestGameRoutingKey;

    public DeleteGuestGameSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /** Every 20 seconds, send a message to delete a guest game */
    @Scheduled(fixedRate = 20000)
    public void sendDeleteGuestGameMessage() {
        Long eventID = 1L; // Example event ID
        rabbitTemplate.convertAndSend(exchangeName, deleteGuestGameRoutingKey, eventID);
    }
}