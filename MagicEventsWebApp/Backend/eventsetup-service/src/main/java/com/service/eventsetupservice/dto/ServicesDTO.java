package com.service.eventsetupservice.dto;

import jakarta.validation.constraints.NotNull;


public class ServicesDTO {
    @NotNull(message = "Board request is required")
    private Boolean board;
    @NotNull(message = "Gallery request is required")
    private Boolean gallery;
    @NotNull(message = "Guest-game request is required")
    private Boolean guestGame;

    public ServicesDTO() {}

    public ServicesDTO(Boolean board, Boolean gallery, Boolean guestGame) {
        this.board = board;
        this.gallery = gallery;
        this.guestGame = guestGame;
    }

    public Boolean getBoard() {
        return this.board;
    }

    public void setBoard(Boolean board) {
        this.board = board;
    }

    public Boolean getGallery() {
        return this.gallery;
    }

    public void setGallery(Boolean gallery) {
        this.gallery = gallery;
    }

    public Boolean getGuestGame() {
        return this.guestGame;
    }

    public void setGuestGame(Boolean guestGame) {
        this.guestGame = guestGame;
    }
}
