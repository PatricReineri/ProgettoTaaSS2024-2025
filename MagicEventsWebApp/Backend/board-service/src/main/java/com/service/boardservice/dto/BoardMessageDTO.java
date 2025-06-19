package com.service.boardservice.dto;

import java.time.LocalDateTime;

public class BoardMessageDTO {
    private Long messageID;
    private String content;
    private String username;
    private LocalDateTime time;

    public BoardMessageDTO() {}

    public BoardMessageDTO(Long messageID, String content, String username, LocalDateTime time) {
        this.messageID = messageID;
        this.content = content;
        this.username = username;
        this.time = time;
    }

    public Long getMessageID() { return messageID; }
    public void setMessageID(Long messageID) { this.messageID = messageID; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}