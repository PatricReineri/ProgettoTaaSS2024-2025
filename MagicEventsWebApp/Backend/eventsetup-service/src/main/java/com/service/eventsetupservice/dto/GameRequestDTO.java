package com.service.eventsetupservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public class GameRequestDTO {
    @NotNull(message = "Event ID cannot be null")
    @Positive(message = "Event ID must be positive")
    private Long eventId;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "User Magic Events tag cannot be null")
    @Positive(message = "User Magic Events tag must be positive")
    private Long userMagicEventsTag;

    public GameRequestDTO() {}

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getUserMagicEventsTag() { return userMagicEventsTag; }
    public void setUserMagicEventsTag(Long userMagicEventsTag) { this.userMagicEventsTag = userMagicEventsTag; }
}
