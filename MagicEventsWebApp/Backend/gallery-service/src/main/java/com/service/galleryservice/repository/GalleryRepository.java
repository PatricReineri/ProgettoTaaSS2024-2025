package com.service.galleryservice.repository;

import com.service.galleryservice.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Gallery findByEventID(Long eventID);
    void deleteByEventID(Long eventID);
}

