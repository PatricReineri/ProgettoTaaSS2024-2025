package com.service.galleryservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.exchange.gallery}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queue.delete-gallery}")
    private String deleteGalleryQueue;

    @Value("${spring.rabbitmq.routing-key.delete-gallery}")
    private String deleteGalleryRoutingKey;

    @Bean
    public Queue deleteGalleryQueue() {
        return new Queue(deleteGalleryQueue, true);
    }

    @Bean
    public Exchange galleryExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding deleteGalleryBinding(Queue deleteGalleryQueue, Exchange galleryExchange) {
        return BindingBuilder.bind(deleteGalleryQueue)
                .to(galleryExchange)
                .with(deleteGalleryRoutingKey)
                .noargs();
    }
}

