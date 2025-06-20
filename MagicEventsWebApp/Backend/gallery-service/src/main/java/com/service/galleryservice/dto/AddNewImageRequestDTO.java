package com.service.galleryservice.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class AddNewImageRequestDTO {
    private Long eventID;
    private String title;
    private String base64Image;
    private String uploadedBy;
    private LocalDateTime dateTime;
    private String magiceventstag;

    public AddNewImageRequestDTO() { }

    public AddNewImageRequestDTO(
            Long eventID,
            String title,
            String base64Image,
            String uploadedBy,
            LocalDateTime dateTime,
            String magiceventstag
    ) {
        if (eventID == null) {
            throw new IllegalArgumentException("eventID cannot be null");
        }
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null");
        }
        if (base64Image == null) {
            throw new IllegalArgumentException("base64Image cannot be null");
        }
        if (uploadedBy == null) {
            throw new IllegalArgumentException("uploadedBy cannot be null");
        }
        this.eventID = eventID;
        this.title = title;
        this.base64Image = base64Image;
        this.uploadedBy = uploadedBy;
        this.dateTime = dateTime;
        this.magiceventstag = magiceventstag;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        if (eventID == null) {
            throw new IllegalArgumentException("eventID cannot be null");
        }
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null");
        }
        this.title = title;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        if (base64Image == null) {
            throw new IllegalArgumentException("base64Image cannot be null");
        }
        this.base64Image = base64Image;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        if (uploadedBy == null) {
            throw new IllegalArgumentException("uploadedBy cannot be null");
        }
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMagiceventstag() {
        return magiceventstag;
    }

    public void setMagiceventstag(String magiceventstag) {
        this.magiceventstag = magiceventstag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddNewImageRequestDTO that = (AddNewImageRequestDTO) o;
        return Objects.equals(eventID, that.eventID) &&
               Objects.equals(title, that.title) &&
               Objects.equals(base64Image, that.base64Image) &&
               Objects.equals(uploadedBy, that.uploadedBy) &&
               Objects.equals(dateTime, that.dateTime) &&
               Objects.equals(magiceventstag, that.magiceventstag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, title, base64Image, uploadedBy, dateTime, magiceventstag);
    }

    @Override
    public String toString() {
        return "AddNewImageRequestDTO{" +
               "eventID=" + eventID +
               ", title='" + title + '\'' +
               ", base64Image='" + base64Image + '\'' +
               ", uploadedBy='" + uploadedBy + '\'' +
               ", dateTime=" + dateTime +
               ", magiceventstag='" + magiceventstag + '\'' +
               '}';
    }
}
