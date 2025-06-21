package com.service.boardservice.controller;

import com.service.boardservice.dto.BoardDTO;
import com.service.boardservice.dto.CreateBoardRequestDTO;
import com.service.boardservice.exception.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/board")
@Validated
public class BoardController {
    private final com.service.boardservice.service.BoardService boardService;

    public BoardController(com.service.boardservice.service.BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/createBoard")
    public ResponseEntity<Boolean> createBoard(@Valid @RequestBody CreateBoardRequestDTO request) {
        try {
            boardService.createBoard(request);
            return ResponseEntity.ok(true);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/getBoard/{eventID}/{pageNumber}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long eventID, 
                                           @PathVariable int pageNumber,
                                           @RequestParam Long userMagicEventsTag) {
        try {
            BoardDTO boardDTO = boardService.getBoard(eventID, userMagicEventsTag, pageNumber, 20);
            if (boardDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(boardDTO);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/isBoardExists/{eventID}")
    public ResponseEntity<Boolean> isBoardExists(@PathVariable Long eventID) {
        boolean exists = boardService.isBoardExists(eventID);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/deleteBoard/{eventID}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long eventID,
                                             @RequestParam Long userMagicEventsTag) {
        try {
            boardService.deleteBoard(eventID, userMagicEventsTag);
            return ResponseEntity.ok(true);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
