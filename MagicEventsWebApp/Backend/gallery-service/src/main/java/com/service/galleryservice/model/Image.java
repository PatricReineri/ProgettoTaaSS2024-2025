package com.service.galleryservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import com.service.galleryservice.model.ImageUserLike;

@Entity
@Table(name = "image")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<ImageUserLike> getImageUserLikes() {
        return imageUserLikes;
    }

    public void setImageUserLikes(List<ImageUserLike> imageUserLikes) {
        this.imageUserLikes = imageUserLikes;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
