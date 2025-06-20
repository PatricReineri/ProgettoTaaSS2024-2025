package com.service.galleryservice.dto;

import java.util.Objects;

public class DeleteImageRequestDTO {
    private Long eventID;
    private Long imageID;
    private String deletedBy;
    private String magiceventstag;

    public DeleteImageRequestDTO() {}

    public DeleteImageRequestDTO(Long eventID, Long imageID, String deletedBy, String magiceventstag) {
        if (eventID == null) {
            throw new IllegalArgumentException("eventID cannot be null");
        }
        if (imageID == null) {
            throw new IllegalArgumentException("imageID cannot be null");
        }
        if (deletedBy == null) {
            throw new IllegalArgumentException("deletedBy cannot be null");
        }
        this.eventID = eventID;
        this.imageID = imageID;
        this.deletedBy = deletedBy;
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

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        if (imageID == null) {
            throw new IllegalArgumentException("imageID cannot be null");
        }
        this.imageID = imageID;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        if (deletedBy == null) {
            throw new IllegalArgumentException("deletedBy cannot be null");
        }
        this.deletedBy = deletedBy;
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
        DeleteImageRequestDTO that = (DeleteImageRequestDTO) o;
        return Objects.equals(eventID, that.eventID) &&
               Objects.equals(imageID, that.imageID) &&
               Objects.equals(deletedBy, that.deletedBy) &&
               Objects.equals(magiceventstag, that.magiceventstag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, imageID, deletedBy, magiceventstag);
    }

    @Override
    public String toString() {
        return "DeleteImageRequestDTO{" +
               "eventID=" + eventID +
               ", imageID=" + imageID +
               ", deletedBy='" + deletedBy + '\'' +
               ", magiceventstag='" + magiceventstag + '\'' +
               '}';
    }
}
