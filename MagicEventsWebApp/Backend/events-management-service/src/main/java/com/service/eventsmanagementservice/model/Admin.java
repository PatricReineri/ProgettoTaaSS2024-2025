package com.service.eventsmanagementservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    private String admin_id;

    @ManyToOne
    @JoinColumn(name = "magic_events_tag")
    private User user;

    @ManyToMany(mappedBy = "admins")
    private List<Event> events;

    public Admin() {}

    public Admin(String adminId, User user, List<Event> events) {
        this.admin_id = adminId;
        this.user = user;
        this.events = events;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String id) {
        this.admin_id = id;
    }

    public List<Event> getEvent() {
        return events;
    }
    public void setEvent(List<Event> events) {
        this.events = events;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}