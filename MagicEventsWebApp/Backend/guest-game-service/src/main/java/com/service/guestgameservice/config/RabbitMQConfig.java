package com.service.guestgameservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.exchange.game}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queue.delete-game}")
    private String deleteGameQueue;

    @Value("${spring.rabbitmq.routing-key.delete-game}")
    private String deleteGameRoutingKey;

    @Bean
    public Queue deleteGameQueue() {
        return new Queue(deleteGameQueue, true);
    }

    @Bean
    public Exchange gameExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding deleteGameBinding(Queue deleteGameQueue, Exchange gameExchange) {
        return BindingBuilder.bind(deleteGameQueue)
                .to(gameExchange)
                .with(deleteGameRoutingKey)
                .noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
