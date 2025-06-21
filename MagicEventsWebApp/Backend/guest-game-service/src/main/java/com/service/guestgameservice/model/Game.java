package com.service.guestgameservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestInfo> guests;

    public Game() {}

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<GuestInfo> getGuests() { return guests; }
    public void setGuests(List<GuestInfo> guests) { this.guests = guests; }
}
