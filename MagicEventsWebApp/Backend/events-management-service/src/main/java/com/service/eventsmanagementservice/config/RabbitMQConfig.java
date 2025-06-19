package com.service.eventsmanagementservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.exchange.event}")
    private String exchangeName;

    @Bean
    public Exchange eventExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

}
