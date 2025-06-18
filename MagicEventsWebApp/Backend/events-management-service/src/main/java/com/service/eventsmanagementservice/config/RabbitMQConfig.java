package com.service.eventsmanagementservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.exchange.event}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queue.delete-event}")
    private String deleteEventQueue;

    @Value("${spring.rabbitmq.routing-key.delete-event}")
    private String deleteEventRoutingKey;

    @Bean
    public Queue deleteEventQueue() {
        return new Queue(deleteEventQueue, true);
    }

    @Bean
    public Exchange eventExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding deleteEventBinding(Queue deleteEventQueue, Exchange eventExchange) {
        return BindingBuilder.bind(deleteEventQueue)
                .to(eventExchange)
                .with(deleteEventRoutingKey)
                .noargs();
    }
}
