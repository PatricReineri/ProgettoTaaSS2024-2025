package com.service.eventsmanagementservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.exchange.event}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queue.delete-ack}")
    private String deleteAckQueue;

    @Value("${spring.rabbitmq.routing-key.delete-ack}")
    private String deleteAckRoutingKey;

    @Bean
    public Exchange eventExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue deleteAckQueue() {
        return new Queue(deleteAckQueue, true);
    }

    @Bean
    public Binding deleteAckBinding(Queue deleteAckQueue, Exchange eventExchange) {
        return BindingBuilder.bind(deleteAckQueue)
                .to(eventExchange)
                .with(deleteAckRoutingKey)
                .noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}