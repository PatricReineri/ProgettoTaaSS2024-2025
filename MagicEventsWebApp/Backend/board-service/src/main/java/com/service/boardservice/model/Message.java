package com.service.boardservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message")
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

    public Message() {}

    public Message(String content, String username, LocalDateTime date, Board board) {
        this.content = content;
        this.Username = username;
        this.date = date;
        this.board = board;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return Username; }
    public void setUsername(String username) { this.Username = username; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", Username='" + Username + '\'' +
                ", date=" + date +
                '}';
    }
}
