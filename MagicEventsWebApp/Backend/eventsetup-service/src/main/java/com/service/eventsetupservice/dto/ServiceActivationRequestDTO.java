package com.service.eventsetupservice.dto;

import jakarta.validation.constraints.NotNull;

public class ServiceActivationRequestDTO {
    @NotNull
    private Long eventId;
    
    @NotNull
    private Long userMagicEventsTag;
    
    private boolean galleryEnabled;
    private boolean boardEnabled;
    private boolean guestGameEnabled;

    // Constructors
    public ServiceActivationRequestDTO() {}

    public ServiceActivationRequestDTO(Long eventId, Long userMagicEventsTag, 
                                     boolean galleryEnabled, boolean boardEnabled, boolean guestGameEnabled) {
        this.eventId = eventId;
        this.userMagicEventsTag = userMagicEventsTag;
        this.galleryEnabled = galleryEnabled;
        this.boardEnabled = boardEnabled;
        this.guestGameEnabled = guestGameEnabled;
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserMagicEventsTag() {
        return userMagicEventsTag;
    }

    public void setUserMagicEventsTag(Long userMagicEventsTag) {
        this.userMagicEventsTag = userMagicEventsTag;
    }

    public boolean isGalleryEnabled() {
        return galleryEnabled;
    }

    public void setGalleryEnabled(boolean galleryEnabled) {
        this.galleryEnabled = galleryEnabled;
    }

    public boolean isBoardEnabled() {
        return boardEnabled;
    }

    public void setBoardEnabled(boolean boardEnabled) {
        this.boardEnabled = boardEnabled;
    }

    public boolean isGuestGameEnabled() {
        return guestGameEnabled;
    }

    public void setGuestGameEnabled(boolean guestGameEnabled) {
        this.guestGameEnabled = guestGameEnabled;
    }
}
