package com.service.galleryservice.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "image_user_like")
public class ImageUserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_magic_events_tag", nullable = false)
    private String userMagicEventsTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

}
