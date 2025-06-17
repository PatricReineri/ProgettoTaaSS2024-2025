package com.service.eventsmanagementservice.dto;

import java.util.List;

public class EventDTO {
    private Long id;

    private String title;

    private String description;

    private String starting;

    private String ending;

    private String location;

    /** User magic_events_tag */
    private Long creator;

    /** User magic_events_tag */
    private List<Long> admins;

    public EventDTO() {}

    public EventDTO(
            String title,
            String description,
            String start,
            String end,
            String location,
            Long creator,
            List<Long> admins
    ) {
        this.title = title;
        this.description = description;
        this.starting = start;
        this.ending = end;
        this.location = location;
        this.creator = creator;
        this.admins = admins;
    }

    public EventDTO(
            Long id,
            String title,
            String description,
            String start,
            String end,
            String location,
            Long creator,
            List<Long> admins
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.starting = start;
        this.ending = end;
        this.location = location;
        this.creator = creator;
        this.admins = admins;
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEnd() {
        return this.ending;
    }

    public String getStart() {
        return this.starting;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLocation() {
        return this.location;
    }

    public Long getCreator() {
        return this.creator;
    }

    public List<Long> getAdmins() {
        return this.admins;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnd(String end) {
        this.ending = end;
    }

    public void setStart(String start) {
        this.starting = start;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public void setAdmins(List<Long> admins) {
        this.admins = admins;
    }
}

