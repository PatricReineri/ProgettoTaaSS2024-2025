package com.service.boardservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String Username;

    @Column(nullable = false)
    private LocalDateTime date;

    /** Foreign key to the board this message belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_event_id", nullable = false)
    private Board board;
}
