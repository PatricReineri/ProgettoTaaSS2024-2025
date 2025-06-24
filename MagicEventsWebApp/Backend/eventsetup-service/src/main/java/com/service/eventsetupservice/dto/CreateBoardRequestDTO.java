package com.service.eventsetupservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public class CreateBoardRequestDTO {
    @NotNull(message = "Event ID cannot be null")
    @Positive(message = "Event ID must be positive")
    private Long eventID;
    
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
    
    @NotNull(message = "User Magic Events tag cannot be null")
    @Positive(message = "User Magic Events tag must be positive")
    private Long userMagicEventsTag;

    public CreateBoardRequestDTO() {}

    public CreateBoardRequestDTO(Long eventID, String title, String description, Long userMagicEventsTag) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
        this.userMagicEventsTag = userMagicEventsTag;
    }

    public Long getEventID() { return eventID; }
    public void setEventID(Long eventID) { this.eventID = eventID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getUserMagicEventsTag() { return userMagicEventsTag; }
    public void setUserMagicEventsTag(Long userMagicEventsTag) { this.userMagicEventsTag = userMagicEventsTag; }
}
