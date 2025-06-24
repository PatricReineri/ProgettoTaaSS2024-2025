package com.service.eventsmanagementservice.dto;

import jakarta.validation.constraints.NotNull;


public class ServicesDTO {
    @NotNull(message = "Board request is required")
    private Boolean board;
    @NotNull(message = "Gallery request is required")
    private Boolean galley;
    @NotNull(message = "Guest-game request is required")
    private Boolean guestGame;

    public ServicesDTO() {}

    public ServicesDTO(Boolean board, Boolean galley, Boolean guestGame) {
        this.board = board;
        this.galley = galley;
        this.guestGame = guestGame;
    }

    public Boolean getBoard() {
        return this.board;
    }

    public void setBoard(Boolean board) {
        this.board = board;
    }

    public Boolean getGalley() {
        return this.galley;
    }

    public void setGalley(Boolean galley) {
        this.galley = galley;
    }

    public Boolean getGuestGame() {
        return this.guestGame;
    }

    public void setGuestGame(Boolean guestGame) {
        this.guestGame = guestGame;
    }
}
