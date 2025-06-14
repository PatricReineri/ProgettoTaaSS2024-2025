package com.service.dummysender.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DeleteBoardSenderService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routingkey.delete-board}")
    private String deleteBoardRoutingKey;

    public DeleteBoardSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // every 10seconds, send a message to delete a board
    @Scheduled(fixedRate = 10000)
    public void sendDeleteBoardMessage() {
        Long eventID = 2L;
        rabbitTemplate.convertAndSend(exchangeName, deleteBoardRoutingKey, eventID);
    }
}
