package com.service.galleryservice.service;

import com.service.galleryservice.dto.AddNewImageRequestDTO;
import com.service.galleryservice.dto.CreateGalleryRequestDTO;
import com.service.galleryservice.dto.GalleryDTO;
import com.service.galleryservice.dto.ImageDTO;
import com.service.galleryservice.dto.ImageUserLikeDTO;
import com.service.galleryservice.model.Gallery;
import com.service.galleryservice.model.Image;
import com.service.galleryservice.repository.GalleryRepository;
import com.service.galleryservice.repository.ImageRepository;
import com.service.galleryservice.repository.ImageUserLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final ImageRepository imageRepository;
    private final ImageUserLikeRepository imageUserLikeRepository;

    public void createGallery(CreateGalleryRequestDTO request) {
        Gallery gallery = new Gallery();
        log.error("1)-----Gallery object created----: {}", gallery);
        gallery.setEventID(request.getEventID());
        log.error("2)-----Gallery set eventID----: {}", gallery.getEventID());
        gallery.setTitle(request.getTitle());
        log.error("3)-----Gallery set title----: {}", gallery.getTitle());
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
