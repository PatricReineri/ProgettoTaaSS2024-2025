package com.service.galleryservice.service;

import com.service.galleryservice.repository.GalleryRepository;
import org.antlr.v4.runtime.misc.Triple;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class DeleteGalleryMessageListener {
    @Value("${spring.rabbitmq.routing-key.delete-ack}")
    private String deleteAckRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final GalleryRepository galleryRepository;

    public DeleteGalleryMessageListener(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-gallery}")
    @Transactional
    public void deleteGallery(Long eventID) {
        try {
            Triple<Long, String, Boolean> response = new Triple<>(eventID, "gallery", true);
            galleryRepository.deleteByEventID(eventID);
            rabbitTemplate.convertAndSend(deleteAckRoutingKey, response);
        } catch (Exception e) {
            Triple<Long, String, Boolean> response = new Triple<>(eventID, "gallery", false);
            rabbitTemplate.convertAndSend(deleteAckRoutingKey, response);
        }
    }
}

