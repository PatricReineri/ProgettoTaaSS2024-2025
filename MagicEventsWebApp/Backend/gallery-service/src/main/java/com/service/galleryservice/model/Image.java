package com.service.galleryservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import com.service.galleryservice.model.ImageUserLike;

@Entity
@Table(name = "image")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "uploaded_by", nullable = false)
    private String uploadedBy;

    @Column(name = "base64_image", nullable = false, length = 10485760) // Adjust length for large base64 strings
    private String base64Image;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    //list of ImageUserLike
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ImageUserLike> imageUserLikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_event_id", nullable = false)
    private Gallery gallery;
}
