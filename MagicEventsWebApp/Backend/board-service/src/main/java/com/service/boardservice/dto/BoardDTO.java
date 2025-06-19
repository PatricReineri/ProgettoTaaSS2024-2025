package com.service.boardservice.dto;

import java.util.List;

public class BoardDTO {
    private Long eventID;
    private String title;
    private String description;
    private List<BoardMessageDTO> messages;

    public BoardDTO() {}

    public BoardDTO(Long eventID, String title, String description, List<BoardMessageDTO> messages) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
        this.messages = messages;
    }

    public Long getEventID() { return eventID; }
    public void setEventID(Long eventID) { this.eventID = eventID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<BoardMessageDTO> getMessages() { return messages; }
    public void setMessages(List<BoardMessageDTO> messages) { this.messages = messages; }
}
