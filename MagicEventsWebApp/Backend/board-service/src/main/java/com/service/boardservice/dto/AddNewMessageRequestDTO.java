package com.service.boardservice.dto;

import java.time.LocalDateTime;

public class AddNewMessageRequestDTO {
    private Long eventID;
    private String content;
    private String username;
    private LocalDateTime dateTime;
    private Long userMagicEventsTag;

    public AddNewMessageRequestDTO() {}

    public AddNewMessageRequestDTO(Long eventID, String content, String username, LocalDateTime dateTime, Long userMagicEventsTag) {
        this.eventID = eventID;
        this.content = content;
        this.username = username;
        this.dateTime = dateTime;
        this.userMagicEventsTag = userMagicEventsTag;
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
}
