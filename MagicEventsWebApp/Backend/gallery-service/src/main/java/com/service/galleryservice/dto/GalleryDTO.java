package com.service.galleryservice.dto;

import java.util.List;
import java.util.Objects;

public class GalleryDTO {
    private Long eventID;
    private String title;
    private List<ImageDTO> images;

    public GalleryDTO() { }

    public GalleryDTO(Long eventID, String title, List<ImageDTO> images) {
        this.eventID = eventID;
        this.title = title;
        this.images = images;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public static GalleryDTOBuilder builder() {
        return new GalleryDTOBuilder();
    }

    public static class GalleryDTOBuilder {
        private Long eventID;
        private String title;
        private List<ImageDTO> images;

        public GalleryDTOBuilder eventID(Long eventID) {
            this.eventID = eventID;
            return this;
        }

        public GalleryDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public GalleryDTOBuilder images(List<ImageDTO> images) {
            this.images = images;
            return this;
        }

        public GalleryDTO build() {
            return new GalleryDTO(eventID, title, images);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GalleryDTO that = (GalleryDTO) o;
        return Objects.equals(eventID, that.eventID) &&
               Objects.equals(title, that.title) &&
               Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, title, images);
    }

    @Override
    public String toString() {
        return "GalleryDTO{" +
               "eventID=" + eventID +
               ", title='" + title + '\'' +
               ", images=" + images +
               '}';
    }
}

