package com.service.eventsmanagementservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private LocalDateTime starting;
    @Column(nullable = false)
    private LocalDateTime ending;
    @Column(name = "magic_events_tag", nullable = false)
    private Long creatorMagicEventsTag;

    private String location;
    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "board_enabled")
    private Boolean boardEnabled;

    @Column(name = "gallery_enabled")
    private Boolean galleryEnabled;

    @Column(name = "guest_game_enabled")
    private Boolean guestGameEnabled;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "magic_events_tag")
    )
    private List<Partecipant> partecipants = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_admins",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private List<Admin> admins = new ArrayList<>(); ;

    public Event() {}

    public Event(
            String title,
            String description,
            LocalDateTime start,
            LocalDateTime end,
            String location,
            Long creator,
            List<Partecipant> partecipants,
            List<Admin> admins,
            String image
    ) {
          this.title = title;
          this.description = description;
          this.starting = start;
          this.ending = end;
          this.location = location;
          this.creatorMagicEventsTag = creator;
          this.partecipants = partecipants;
          this.admins = admins;
          this.image = image;
          this.status = "ACTIVE";
    }

    public Event(
            String title,
            String description,
            LocalDateTime start,
            LocalDateTime end,
            String location,
            Long creator,
            String image
    ) {
        this.title = title;
        this.description = description;
        this.starting = start;
        this.ending = end;
        this.location = location;
        this.creatorMagicEventsTag = creator;
        this.image = image;
        this.status = "ACTIVE";
    }

    public Long getEventId() {
        return this.event_id;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getEnding() {
        return this.ending;
    }

    public LocalDateTime getStarting() {
        return this.starting;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLocation() {
        return this.location;
    }

    public Long getCreator() {
        return this.creatorMagicEventsTag;
    }

    public List<Admin> getAdmins() {
        return this.admins;
    }

    public List<Partecipant> getPartecipants() {
        return this.partecipants;
    }

    public String getStatus() {
        return this.status;
    }

    public String getImage() {
        return this.image;
    }

    public Boolean getBoardEnabled() {
        return this.boardEnabled;
    }

    public Boolean getGalleryEnabled() {
        return this.galleryEnabled;
    }

    public Boolean getGuestGameEnabled() {
        return this.guestGameEnabled;
    }

    public void setEventId(Long event_id) {
        this.event_id = event_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnding(LocalDateTime end) {
        this.ending = end;
    }

    public void setStarting(LocalDateTime start) {
        this.starting = start;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreator(Long creator) {
        this.creatorMagicEventsTag = creator;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public void setPartecipants(List<Partecipant> partecipants) {
        this.partecipants = partecipants;
    }



    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBoardEnabled(Boolean boardEnabled) {
        this.boardEnabled = boardEnabled;
    }

    public void setGalleryEnabled(Boolean galleryEnabled) {
        this.galleryEnabled = galleryEnabled;
    }

    public void setGuestGameEnabled(Boolean guestGameEnabled) {
        this.guestGameEnabled = guestGameEnabled;
    }
}
