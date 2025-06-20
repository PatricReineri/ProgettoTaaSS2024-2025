package com.service.boardservice.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "board")
public class Board {
    @Id
    @Column(name = "event_id", nullable = false)
    private long eventID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    /** Foreign key to a list of messages of the board */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Message> messages;

    public Board() {}

    public Board(long eventID, String title, String description) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
    }

    public long getEventID() { return eventID; }
    public void setEventID(long eventID) { this.eventID = eventID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return eventID == board.eventID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID);
    }

    @Override
    public String toString() {
        return "Board{" +
                "eventID=" + eventID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}