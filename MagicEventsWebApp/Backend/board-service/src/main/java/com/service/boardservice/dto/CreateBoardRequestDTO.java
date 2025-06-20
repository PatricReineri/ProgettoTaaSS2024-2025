package com.service.boardservice.dto;

public class CreateBoardRequestDTO {
    private Long eventID;
    private String title;
    private String description;

    public CreateBoardRequestDTO() {}

    public CreateBoardRequestDTO(Long eventID, String title, String description) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
    }

    public Long getEventID() { return eventID; }
    public void setEventID(Long eventID) { this.eventID = eventID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
