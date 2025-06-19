package com.service.galleryservice.service;

import com.service.galleryservice.dto.AddNewImageRequestDTO;
import com.service.galleryservice.dto.DeleteImageRequestDTO;
import com.service.galleryservice.dto.ImageLikeRequestDTO;
import com.service.galleryservice.model.Gallery;
import com.service.galleryservice.model.Image;
import com.service.galleryservice.model.ImageUserLike;
import com.service.galleryservice.repository.GalleryRepository;
import com.service.galleryservice.repository.ImageRepository;
import com.service.galleryservice.repository.ImageUserLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageChatService {
    private final GalleryRepository galleryRepository;
    private final ImageRepository imageRepository;
    private final ImageUserLikeRepository imageUserLikeRepository;

    public AddNewImageRequestDTO addNewImage(AddNewImageRequestDTO request) {
        log.info("Adding new image to gallery for event ID: {}", request.getEventID());
        Gallery gallery = galleryRepository.findByEventID(request.getEventID());

        if (gallery == null) {
            log.warn("Gallery not found for event ID, can't add new image: {}", request.getEventID());
            return null;
        }

        Image image = new Image();
        image.setTitle(request.getTitle());
        image.setBase64Image(request.getBase64Image());
        // set current or provided timestamp
        image.setDateTime(request.getDateTime() != null ? request.getDateTime() : LocalDateTime.now());
        image.setUploadedBy(request.getUploadedBy());
        image.setGallery(gallery);
        imageRepository.save(image);
        log.info("Image added successfully to gallery for event ID: {}", request.getEventID());
        // propagate saved timestamp back to DTO
        request.setDateTime(image.getDateTime());
        return request;
    }

    public DeleteImageRequestDTO deleteImage(DeleteImageRequestDTO request) {
        log.info("Deleting image with ID: {}", request.getImageID());
        Image image = imageRepository.findById(request.getImageID())
                .orElseThrow(() -> new IllegalArgumentException("Image not found with ID: " + request.getImageID()));
        Gallery gallery = image.getGallery();
        if (gallery == null) {
            log.warn("Gallery not found for image ID: {}, can't delete image", request.getImageID());
            return null;
        }
        imageRepository.delete(image);

        log.info("Image with ID: {} deleted successfully from gallery for event ID: {}", request.getImageID(), gallery.getEventID());

        return request;
    }

    public ImageLikeRequestDTO handleImageLike(ImageLikeRequestDTO request) {
        log.info("Processing image like/unlike: {}", request);
        Image image = imageRepository.findById(request.getImageID())
                .orElseThrow(() -> new IllegalArgumentException("Image not found with ID: " + request.getImageID()));
        if (request.isLike()) {
            boolean exists = !imageUserLikeRepository.findByImageAndUserMagicEventsTag(image, request.getUserMagicEventsTag()).isEmpty();
            if (!exists) {
                ImageUserLike newLike = new ImageUserLike();
                newLike.setImage(image);
                newLike.setUserMagicEventsTag(request.getUserMagicEventsTag());
                imageUserLikeRepository.save(newLike);
            }
        } else {
            var likes = imageUserLikeRepository.findByImageAndUserMagicEventsTag(image, request.getUserMagicEventsTag());
            if (!likes.isEmpty()) {
                imageUserLikeRepository.deleteAll(likes);
            }
        }

        request.setLikedCount(imageUserLikeRepository.countByImage(image));
        return request;
    }

}
