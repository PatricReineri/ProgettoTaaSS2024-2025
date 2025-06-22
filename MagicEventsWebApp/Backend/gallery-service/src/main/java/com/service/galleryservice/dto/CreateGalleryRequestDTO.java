package com.service.galleryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public class CreateGalleryRequestDTO {
    @NotNull(message = "Event ID cannot be null")
    private Long eventID;
    
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    @NotNull(message = "User Magic Events Tag cannot be null")
    private Long userMagicEventsTag;

    public CreateGalleryRequestDTO() {}

    public CreateGalleryRequestDTO(Long eventID, String title, Long userMagicEventsTag) {
        if (eventID == null) {
            throw new IllegalArgumentException("eventID cannot be null");
        }
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null");
        }
        if (userMagicEventsTag == null) {
            throw new IllegalArgumentException("userMagicEventsTag cannot be null");
        }
        this.eventID = eventID;
        this.title = title;
        this.userMagicEventsTag = userMagicEventsTag;
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

    public Long getUserMagicEventsTag() {
        return userMagicEventsTag;
    }

    public void setUserMagicEventsTag(Long userMagicEventsTag) {
        if (userMagicEventsTag == null) {
            throw new IllegalArgumentException("userMagicEventsTag cannot be null");
        }
        this.userMagicEventsTag = userMagicEventsTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateGalleryRequestDTO that = (CreateGalleryRequestDTO) o;
        return Objects.equals(eventID, that.eventID) &&
               Objects.equals(title, that.title) &&
               Objects.equals(userMagicEventsTag, that.userMagicEventsTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, title, userMagicEventsTag);
    }

    @Override
    public String toString() {
        return "CreateGalleryRequestDTO{" +
               "eventID=" + eventID +
               ", title='" + title + '\'' +
               ", userMagicEventsTag=" + userMagicEventsTag +
               '}';
    }
}
