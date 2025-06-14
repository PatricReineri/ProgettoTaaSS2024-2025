package com.service.boardservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.exchange.board}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queue.delete-board}")
    private String deleteBoardQueue;

    @Value("${spring.rabbitmq.routing-key.delete-board}")
    private String deleteBoardRoutingKey;

    @Bean
    public Queue deleteBoardQueue() {
        return new Queue(deleteBoardQueue, true);
    }

    @Bean
    public Exchange boardExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding deleteBoardBinding(Queue deleteBoardQueue, Exchange boardExchange) {
        return BindingBuilder.bind(deleteBoardQueue)
                .to(boardExchange)
                .with(deleteBoardRoutingKey)
                .noargs();
    }
}
