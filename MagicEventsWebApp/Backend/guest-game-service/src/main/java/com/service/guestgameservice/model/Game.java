package com.service.guestgameservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "game")
@Data
public class Game {
    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestInfo> guests;
}
