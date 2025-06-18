package com.service.eventsmanagementservice.dto;

import java.util.ArrayList;
import java.util.List;

public class EventDTO {
    private String title;

    private String description;

    private String starting;

    private String ending;

    private String location;

    /** User magic_events_tag */
    private Long creator;

    private ArrayList<Long> partecipants;

    private ArrayList<Long> admins;

    public EventDTO() {}

    public EventDTO(
            String title,
            String description,
            String start,
            String end,
            String location,
            Long creator,
            ArrayList<Long> partecipants,
            ArrayList<Long> admins
    ) {
        this.title = title;
        this.description = description;
        this.starting = start;
        this.ending = end;
        this.location = location;
        this.creator = creator;
        this.partecipants = partecipants;
        this.admins = admins;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEnding() {
        return this.ending;
    }

    public String getStarting() {
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

    public List<Long> getPartecipants() {
        return this.partecipants;
    }

    public ArrayList<Long> getAdmins() {
        return this.admins;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnding(String end) {
        this.ending = end;
    }

    public void setStarting(String start) {
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

    public void setAdmins(ArrayList<Long> admins) {
        this.admins = admins;
    }

    public void setPartecipants(ArrayList<Long> partecipants) {
        this.partecipants = partecipants;
    }
}

