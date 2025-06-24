package com.service.galleryservice.service;

import com.service.galleryservice.repository.GalleryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class DeleteGalleryMessageListener {
    private final GalleryRepository galleryRepository;

    public DeleteGalleryMessageListener(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-gallery}")
    @Transactional
    public void deleteGallery(Long eventID) {
        try {
            galleryRepository.deleteByEventID(eventID);
        } catch (Exception e) {
            // Handle error silently or throw if needed
        }
    }
}

