package com.service.guestgameservice.dto;

public class GameRequestDTO {
    private Long eventId;
    private String description;
    private Long userMagicEventsTag;

    public GameRequestDTO() {}

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getUserMagicEventsTag() { return userMagicEventsTag; }
    public void setUserMagicEventsTag(Long userMagicEventsTag) { this.userMagicEventsTag = userMagicEventsTag; }
}
