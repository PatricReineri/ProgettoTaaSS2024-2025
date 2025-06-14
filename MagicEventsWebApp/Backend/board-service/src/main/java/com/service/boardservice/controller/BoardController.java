package com.service.boardservice.controller;

import com.service.boardservice.dto.AddNewMessageRequestDTO;
import com.service.boardservice.dto.BoardDTO;
import com.service.boardservice.dto.CreateBoardRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final com.service.boardservice.service.BoardService boardService;

    @PostMapping("/createBoard")
    public ResponseEntity<Boolean> createBoard(@RequestBody CreateBoardRequestDTO request) {
        try {
            boardService.createBoard(request);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            log.error("Error creating board: {}", e.getMessage());
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

    @PostMapping("/writeMessage")
    public ResponseEntity<Void> writeMessage(@RequestBody AddNewMessageRequestDTO request) {
        boardService.addNewMessage(request);
        return ResponseEntity.ok().build();
    }
}
