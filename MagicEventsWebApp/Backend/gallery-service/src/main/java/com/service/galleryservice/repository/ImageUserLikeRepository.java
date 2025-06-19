package com.service.galleryservice.repository;

import com.service.galleryservice.model.Image;
import com.service.galleryservice.model.ImageUserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageUserLikeRepository extends JpaRepository<ImageUserLike, Long> {
    List<ImageUserLike> findByImage(Image image);
    List<ImageUserLike> findByImageAndUserMagicEventsTag(Image image, String userMagicEventsTag);

    int countByImage(Image image);
}
