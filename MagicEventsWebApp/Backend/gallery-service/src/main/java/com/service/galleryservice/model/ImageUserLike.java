package com.service.galleryservice.model;

import jakarta.persistence.*;

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

    public ImageUserLike() { }

    public ImageUserLike(String userMagicEventsTag, Image image) {
        this.userMagicEventsTag = userMagicEventsTag;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserMagicEventsTag() {
        return userMagicEventsTag;
    }

    public void setUserMagicEventsTag(String userMagicEventsTag) {
        this.userMagicEventsTag = userMagicEventsTag;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
