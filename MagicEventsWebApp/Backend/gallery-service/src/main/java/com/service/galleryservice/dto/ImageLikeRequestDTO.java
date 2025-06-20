package com.service.galleryservice.dto;

public class ImageLikeRequestDTO {
    private String userMagicEventsTag;
    private boolean like;
    private Long imageID;
    private Long eventID;

    private int likedCount = 0;

    public ImageLikeRequestDTO() { }

    public ImageLikeRequestDTO(String userMagicEventsTag, boolean like, Long imageID, Long eventID, int likedCount) {
        this.userMagicEventsTag = userMagicEventsTag;
        this.like = like;
        this.imageID = imageID;
        this.eventID = eventID;
        this.likedCount = likedCount;
    }

    public String getUserMagicEventsTag() {
        return userMagicEventsTag;
    }

    public void setUserMagicEventsTag(String userMagicEventsTag) {
        this.userMagicEventsTag = userMagicEventsTag;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }
}
