package com.service.boardservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class AddNewMessageRequestDTO {
    @NotNull(message = "Event ID cannot be null")
    @Positive(message = "Event ID must be positive")
    private Long eventID;
    
    @NotBlank(message = "Content cannot be blank")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;
    
    private LocalDateTime dateTime;
    
    @NotNull(message = "User Magic Events tag cannot be null")
    @Positive(message = "User Magic Events tag must be positive")
    private Long userMagicEventsTag;

    private Long messageID;

    public AddNewMessageRequestDTO() {}

    public AddNewMessageRequestDTO(Long eventID, String content, String username, LocalDateTime dateTime, Long userMagicEventsTag, Long messageID) {
        this.eventID = eventID;
        this.content = content;
        this.username = username;
        this.dateTime = dateTime;
        this.userMagicEventsTag = userMagicEventsTag;
        this.messageID = messageID;
    }

    public Long getEventID() { return eventID; }
    public void setEventID(Long eventID) { this.eventID = eventID; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public Long getUserMagicEventsTag() { return userMagicEventsTag; }
    public void setUserMagicEventsTag(Long userMagicEventsTag) { this.userMagicEventsTag = userMagicEventsTag; }

    public Long getMessageID() { return messageID; }
    public void setMessageID(Long messageID) { this.messageID = messageID; }
}
