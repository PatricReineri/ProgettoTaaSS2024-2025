package com.service.boardservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardDTO {
    private Long eventID;
    private String title;
    private String description;
    private List<BoardMessageDTO> messages;
}
