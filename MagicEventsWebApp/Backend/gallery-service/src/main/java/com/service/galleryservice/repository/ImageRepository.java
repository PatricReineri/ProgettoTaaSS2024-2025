package com.service.galleryservice.repository;

import com.service.galleryservice.model.Gallery;
import com.service.galleryservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByGallery(Gallery gallery);
}

