package com.service.galleryservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "gallery")
public class Gallery {
    @Id
    @Column(name = "event_id", nullable = false)
    private long eventID;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Image> images;

    // explicit constructors
    public Gallery() {
    }

    public Gallery(long eventID, String title, List<Image> images) {
        this.eventID = eventID;
        this.title = title;
        this.images = images;
    }

    // getters and setters
    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
