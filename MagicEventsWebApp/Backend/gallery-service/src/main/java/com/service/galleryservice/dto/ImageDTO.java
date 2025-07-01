package com.service.galleryservice.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ImageDTO {
    private Long imageID;
    private String title;
    private String base64Image;
    private String uploadedBy;
    private LocalDateTime dateTime;
    private int likes;
    private boolean userLike;

    public ImageDTO() { }

    public ImageDTO(Long imageID, String title, String base64Image, String uploadedBy, LocalDateTime dateTime, int likes) {
        this.imageID = imageID;
        this.title = title;
        this.base64Image = base64Image;
        this.uploadedBy = uploadedBy;
        this.dateTime = dateTime;
        this.likes = likes;
    }

    public ImageDTO(Long imageID, String title, String base64Image, String uploadedBy, LocalDateTime dateTime, int likes, boolean userLike) {
        this.imageID = imageID;
        this.title = title;
        this.base64Image = base64Image;
        this.uploadedBy = uploadedBy;
        this.dateTime = dateTime;
        this.likes = likes;
        this.userLike = userLike;
    }

    public Long getimageID() {
        return imageID;
    }

    public void setimageID(Long imageID) {
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isUserLike() {
        return userLike;
    }

    public void setUserLike(boolean userLike) {
        this.userLike = userLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDTO imageDTO = (ImageDTO) o;
        return likes == imageDTO.likes &&
               userLike == imageDTO.userLike &&
               Objects.equals(imageID, imageDTO.imageID) &&
               Objects.equals(title, imageDTO.title) &&
               Objects.equals(base64Image, imageDTO.base64Image) &&
               Objects.equals(uploadedBy, imageDTO.uploadedBy) &&
               Objects.equals(dateTime, imageDTO.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageID, title, base64Image, uploadedBy, dateTime, likes, userLike);
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
               "imageID=" + imageID +
               ", title='" + title + '\'' +
               ", base64Image='" + base64Image + '\'' +
               ", uploadedBy='" + uploadedBy + '\'' +
               ", dateTime=" + dateTime +
               ", likes=" + likes +
               ", userLike=" + userLike +
               '}';
    }
}
