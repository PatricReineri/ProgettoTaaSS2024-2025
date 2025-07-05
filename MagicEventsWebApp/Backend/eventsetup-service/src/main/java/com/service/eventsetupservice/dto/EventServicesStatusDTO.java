package com.service.eventsetupservice.dto;

public class EventServicesStatusDTO {
    private Long eventId;
    private boolean eventCreated;
    private boolean boardCreated;
    private boolean galleryCreated;
    private boolean gameCreated;
    private String errorMessage;

    public EventServicesStatusDTO() {
        this.eventCreated = false;
        this.boardCreated = false;
        this.galleryCreated = false;
        this.gameCreated = false;
    }

    public EventServicesStatusDTO(
            Long eventId,
            boolean eventCreated
    ) {
        this.eventId = eventId;
        this.eventCreated = eventCreated;
        this.boardCreated = false;
        this.galleryCreated = false;
        this.gameCreated = false;
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public boolean isEventCreated() { return eventCreated; }
    public void setEventCreated(boolean eventCreated) { this.eventCreated = eventCreated; }

    public boolean isBoardCreated() { return boardCreated; }
    public void setBoardCreated(boolean boardCreated) { this.boardCreated = boardCreated; }

    public boolean isGalleryCreated() { return galleryCreated; }
    public void setGalleryCreated(boolean galleryCreated) { this.galleryCreated = galleryCreated; }

    public boolean isGameCreated() { return gameCreated; }
    public void setGameCreated(boolean gameCreated) { this.gameCreated = gameCreated; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean isSetupSuccessful() {
        return eventCreated && boardCreated;
    }
}
