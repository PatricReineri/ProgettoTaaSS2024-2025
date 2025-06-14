package com.service.boardservice.dto;

import lombok.Data;

@Data
public class CreateBoardRequestDTO {
    private Long eventID;
    private String title;
    private String description;
}
