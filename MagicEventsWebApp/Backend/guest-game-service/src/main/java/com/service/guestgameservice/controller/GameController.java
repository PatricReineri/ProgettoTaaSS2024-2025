package com.service.guestgameservice.controller;

import com.service.guestgameservice.dto.GameRequestDTO;
import com.service.guestgameservice.dto.GuestInfoRequestDTO;
import com.service.guestgameservice.dto.DecisionTreeDTO;
import com.service.guestgameservice.service.GameService;

import jakarta.validation.Valid;

import com.service.guestgameservice.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest-game")
@Validated
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @DeleteMapping("/deleteGame/{eventId}")
    public ResponseEntity<Boolean> deleteGame(@PathVariable Long eventId, @RequestParam Long userMagicEventsTag) {
        try {
            gameService.deleteGame(eventId, userMagicEventsTag);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gameExists/{eventId}")
    public ResponseEntity<Boolean> gameExists(@PathVariable Long eventId) {
        try {
            boolean exists = gameService.gameExists(eventId);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insertGuestInfo")
    public ResponseEntity<Void> insertGuestInfo(@Valid @RequestBody GuestInfoRequestDTO guestInfoRequestDTO) {
        try {
            gameService.insertGuestInfo(guestInfoRequestDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createGame")
    public ResponseEntity<Void> createGame(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        try {
            gameService.createGame(gameRequestDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/createDecisionTree/{eventId}")
    public ResponseEntity<DecisionTreeDTO> createDecisionTree(@PathVariable Long eventId, @RequestParam Long userMagicEventsTag) {
        try {
            DecisionTreeDTO decisionTree = gameService.createDecisionTree(eventId, userMagicEventsTag);
            return new ResponseEntity<>(decisionTree, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
