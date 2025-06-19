package com.service.galleryservice.service;

import com.service.galleryservice.dto.CreateGalleryRequestDTO;
import com.service.galleryservice.dto.GalleryDTO;
import com.service.galleryservice.dto.ImageDTO;
import com.service.galleryservice.dto.ImageUserLikeDTO;
import com.service.galleryservice.exception.UnauthorizedException;
import com.service.galleryservice.model.Gallery;
import com.service.galleryservice.repository.GalleryRepository;
import com.service.galleryservice.repository.ImageRepository;
import com.service.galleryservice.repository.ImageUserLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final ImageRepository imageRepository;
    private final ImageUserLikeRepository imageUserLikeRepository;
    private final WebClient eventManagementWebClient;

    public void createGallery(CreateGalleryRequestDTO request) {
        if (!authorizeCreateGallery(request.getEventID(), request.getUserMagicEventsTag())) {
            throw new UnauthorizedException("Not authorized to create gallery for event ID: " + request.getEventID());
        }
        Gallery gallery = new Gallery();
        gallery.setEventID(request.getEventID());
        gallery.setTitle(request.getTitle());
        galleryRepository.save(gallery);
    }

    public GalleryDTO getGallery(Long eventID, int pageNumber, int pageSize) {
        log.info("Fetching gallery for event ID: {}", eventID);
        Gallery gallery = galleryRepository.findByEventID(eventID);
        if (gallery == null) {
            log.warn("Gallery not found for event ID: {}", eventID);
            return null;
        }

        List<ImageDTO> images = imageRepository.findByGallery(gallery)
                .stream()
                .sorted((i1, i2) -> i2.getDateTime().compareTo(i1.getDateTime()))
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .map(img -> ImageDTO.builder()
                        .id(img.getId())
                        .title(img.getTitle())
                        .base64Image(img.getBase64Image())
                        .dateTime(img.getDateTime())
                        .uploadedBy(img.getUploadedBy())
                        .imageUserLikes(
                                imageUserLikeRepository.findByImage(img).stream()
                                        .map(uil -> new ImageUserLikeDTO(uil.getUserMagicEventsTag()))
                                        .toList()
                        )
                        .build()
                )
                .toList();

        return GalleryDTO.builder()
                .eventID(gallery.getEventID())
                .title(gallery.getTitle())
                .images(images)
                .build();
    }


    public GalleryDTO getMostPopularImages(Long eventID, int pageNumber, int pageSize) {
        log.info("Fetching most popular images for event ID: {}", eventID);
        Gallery gallery = galleryRepository.findByEventID(eventID);
        if (gallery == null) {
            log.warn("Gallery not found for event ID: {}", eventID);
            return null;
        }

        List<ImageDTO> images = imageRepository.findByGallery(gallery)
                .stream()
                .sorted((i1, i2) -> Integer.compare(
                        imageUserLikeRepository.countByImage(i2),
                        imageUserLikeRepository.countByImage(i1)))
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .map(img -> ImageDTO.builder()
                        .id(img.getId())
                        .title(img.getTitle())
                        .base64Image(img.getBase64Image())
                        .dateTime(img.getDateTime())
                        .uploadedBy(img.getUploadedBy())
                        .imageUserLikes(
                                imageUserLikeRepository.findByImage(img).stream()
                                        .map(uil -> new ImageUserLikeDTO(uil.getUserMagicEventsTag()))
                                        .toList()
                        )
                        .build()
                )
                .toList();

        return GalleryDTO.builder()
                .eventID(gallery.getEventID())
                .title(gallery.getTitle())
                .images(images)
                .build();
    }

    private boolean authorizeCreateGallery(Long eventID, Long userMagicEventsTag) {
        log.info("Authorizing gallery creation for event ID: {} by user: {}", eventID, userMagicEventsTag);
        try {
            Boolean isAdmin = eventManagementWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gestion/isAdmin")
                            .queryParam("partecipantId", userMagicEventsTag)
                            .queryParam("eventId", eventID)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return Boolean.TRUE.equals(isAdmin);
        } catch (Exception e) {
            log.error("Error authorizing gallery creation for event ID {} by user {}: {}", eventID, userMagicEventsTag, e.getMessage());
            return false;
        }
    }

    public Boolean isGalleryExists(Long eventID) {
        log.info("Checking if gallery exists for event ID: {}", eventID);
        return galleryRepository.findByEventID(eventID) != null;
    }

    @Transactional
    public void deleteGallery(Long eventID) {
        log.info("Deleting gallery for event ID: {}", eventID);
        Gallery gallery = galleryRepository.findByEventID(eventID);
        if (gallery != null) {
            galleryRepository.delete(gallery);
            log.info("Gallery and its images deleted successfully for event ID: {}", eventID);
        } else {
            log.warn("No gallery found for event ID: {}", eventID);
        }
    }
}
