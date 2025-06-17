package com.service.eventsmanagementservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "event_info")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long event_id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String starting;
    @Column(nullable = false)
    private String ending;

    private String location;

    @ManyToOne
    @JoinColumn(name = "magic_events_tag")
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "event_admins",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private List<Admin> admins;

    public Event() {}

    public Event(
            String title,
            String description,
            String start,
            String end,
            String location,
            User creator,
            List<Admin> admins
    ) {
      this.title = title;
      this.description = description;
      this.starting = start;
      this.ending = end;
      this.location = location;
      this.creator = creator;
      this.admins = admins;
    }

    public Long getEventId() {
        return this.event_id;
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

    public User getCreator() {
        return this.creator;
    }

    public List<Admin> getAdmins() {
        return this.admins;
    }

    public void setEventId(Long event_id) {
        this.event_id = event_id;
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

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }
}
