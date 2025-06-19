package com.service.galleryservice.controller;

import com.service.galleryservice.dto.GalleryDTO;
import com.service.galleryservice.dto.CreateGalleryRequestDTO;
import com.service.galleryservice.service.GalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gallery")
@RequiredArgsConstructor
@Slf4j
public class GalleryController {
    private final GalleryService galleryService;

    @PostMapping("/createGallery")
    public ResponseEntity<Boolean> createGallery(@RequestBody CreateGalleryRequestDTO request) {
        try {
            galleryService.createGallery(request);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            log.error("Error creating gallery: {}", e.getMessage());
            return ResponseEntity.status(500).body(false);
        }
    }

    @GetMapping("/getGallery/{eventID}/{pageNumber}")
    public ResponseEntity<GalleryDTO> getGallery(@PathVariable Long eventID, @PathVariable int pageNumber) {
        GalleryDTO galleryDTO = galleryService.getGallery(eventID, pageNumber, 20);
        if (galleryDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(galleryDTO);
    }

    @GetMapping("/isGalleryExists/{eventID}")
    public ResponseEntity<Boolean> isGalleryExists(@PathVariable Long eventID) {
        boolean exists = galleryService.isGalleryExists(eventID);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/deleteGallery/{eventID}")
    public ResponseEntity<Boolean> deleteGallery(@PathVariable Long eventID) {
        try {
            galleryService.deleteGallery(eventID);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            log.error("Error deleting gallery: {}", e.getMessage());
            return ResponseEntity.status(500).body(false);
        }
    }
}
