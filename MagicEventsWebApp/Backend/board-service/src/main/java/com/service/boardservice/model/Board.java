package com.service.boardservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "board")
@Data
public class Board {
    @Id
    @Column(name = "event_id", nullable = false)
    private long eventID;

    @Column(nullable = false)
    private  String title;

    @Column(nullable = false)
    private String description;

    /** Foreign key to a list of messages of the board */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Message> messages;

}