package com.service.boardservice.controller;

import com.service.boardservice.dto.BoardDTO;
import com.service.boardservice.dto.CreateBoardRequestDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final com.service.boardservice.service.BoardService boardService;

    public BoardController(com.service.boardservice.service.BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/createBoard")
    public ResponseEntity<Boolean> createBoard(@RequestBody CreateBoardRequestDTO request) {
        try {
            boardService.createBoard(request);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @GetMapping("/getBoard/{eventID}/{pageNumber}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long eventID, @PathVariable int pageNumber) {
        BoardDTO boardDTO = boardService.getBoard(eventID, pageNumber, 20);
        if (boardDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boardDTO);
    }

    @GetMapping("/isBoardExists/{eventID}")
    public ResponseEntity<Boolean> isBoardExists(@PathVariable Long eventID) {
        boolean exists = boardService.isBoardExists(eventID);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/deleteBoard/{eventID}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long eventID) {
        try {
            boardService.deleteBoard(eventID);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }
}
