package com.service.galleryservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "gallery")
@Data
public class Gallery {
    @Id
    @Column(name = "event_id", nullable = false)
    private long eventID;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Image> images;
}

