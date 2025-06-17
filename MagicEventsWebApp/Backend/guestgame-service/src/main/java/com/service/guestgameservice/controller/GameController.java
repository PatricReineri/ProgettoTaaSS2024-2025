package com.service.guestgameservice.controller;

import com.service.guestgameservice.dto.GameRequestDTO;
import com.service.guestgameservice.dto.GuestInfoRequestDTO;
import com.service.guestgameservice.dto.DecisionTreeDTO;
import com.service.guestgameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest-game")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;


    @DeleteMapping("/deleteGame/{eventId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long eventId) {
        gameService.deleteGame(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/gameExists/{eventId}")
    public ResponseEntity<Boolean> gameExists(@PathVariable Long eventId) {
        boolean exists = gameService.gameExists(eventId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @PostMapping("/insertGuestInfo")
    public ResponseEntity<Void> insertGuestInfo(@RequestBody GuestInfoRequestDTO guestInfoRequestDTO) {
        gameService.insertGuestInfo(guestInfoRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/createGame")
    public ResponseEntity<Void> createGame(@RequestBody GameRequestDTO gameRequestDTO) {
        gameService.createGame(gameRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/createDecisionTree")
    public ResponseEntity<DecisionTreeDTO> createDecisionTree() {
        DecisionTreeDTO decisionTree = gameService.createDecisionTree();
        return new ResponseEntity<>(decisionTree, HttpStatus.OK);
    }
}
