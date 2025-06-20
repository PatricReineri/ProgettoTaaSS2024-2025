package com.service.boardservice.dto;

import java.util.Objects;

public class DeleteMessageRequestDTO {
    private Long eventID;
    private Long messageID;
    private String deletedBy;
    private Long userMagicEventsTag;

    public DeleteMessageRequestDTO() { }

    public DeleteMessageRequestDTO(Long eventID, Long messageID, String deletedBy, Long userMagicEventsTag) {
        if (eventID == null) {
            throw new IllegalArgumentException("eventID cannot be null");
        }
        if (messageID == null) {
            throw new IllegalArgumentException("messageID cannot be null");
        }
        if (deletedBy == null) {
            throw new IllegalArgumentException("deletedBy cannot be null");
        }
        this.eventID = eventID;
        this.messageID = messageID;
        this.deletedBy = deletedBy;
        this.userMagicEventsTag = userMagicEventsTag;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        if (eventID == null) {
            throw new IllegalArgumentException("eventID cannot be null");
        }
        this.eventID = eventID;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        if (messageID == null) {
            throw new IllegalArgumentException("messageID cannot be null");
        }
        this.messageID = messageID;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        if (deletedBy == null) {
            throw new IllegalArgumentException("deletedBy cannot be null");
        }
        this.deletedBy = deletedBy;
    }

    public Long getUserMagicEventsTag() {
        return userMagicEventsTag;
    }

    public void setUserMagicEventsTag(Long userMagicEventsTag) {
        this.userMagicEventsTag = userMagicEventsTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteMessageRequestDTO that = (DeleteMessageRequestDTO) o;
        return Objects.equals(eventID, that.eventID) &&
               Objects.equals(messageID, that.messageID) &&
               Objects.equals(deletedBy, that.deletedBy) &&
               Objects.equals(userMagicEventsTag, that.userMagicEventsTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, messageID, deletedBy, userMagicEventsTag);
    }

    @Override
    public String toString() {
        return "DeleteMessageRequestDTO{" +
               "eventID=" + eventID +
               ", messageID=" + messageID +
               ", deletedBy='" + deletedBy + '\'' +
               ", userMagicEventsTag=" + userMagicEventsTag +
               '}';
    }
}
