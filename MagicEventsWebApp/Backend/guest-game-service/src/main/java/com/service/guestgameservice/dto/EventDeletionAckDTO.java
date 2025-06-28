package com.service.guestgameservice.dto;

import java.io.Serializable;

public class EventDeletionAckDTO implements Serializable {
    private Long eventId;
    private String serviceType;
    private Boolean isSuccess;

    public EventDeletionAckDTO() {}

    public EventDeletionAckDTO(Long eventId, String serviceType, Boolean isSuccess) {
        this.eventId = eventId;
        this.serviceType = serviceType;
        this.isSuccess = isSuccess;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
