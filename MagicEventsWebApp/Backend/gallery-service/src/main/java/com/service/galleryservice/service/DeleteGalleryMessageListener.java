package com.service.galleryservice.service;

import com.service.galleryservice.repository.GalleryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteGalleryMessageListener {
    private final GalleryRepository galleryRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-gallery}")
    @Transactional
    public void deleteGallery(Long eventID) {
        log.info("Received delete gallery message for eventID: {}", eventID);
        try {
            galleryRepository.deleteByEventID(eventID);
            log.info("Gallery and its images with eventID {} deleted successfully", eventID);
        } catch (Exception e) {
            log.error("Error deleting gallery with eventID {}: {}", eventID, e.getMessage());
        }
    }
}

