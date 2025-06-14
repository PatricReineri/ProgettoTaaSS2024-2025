package com.service.boardservice.dto;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class AddNewMessageRequestDTO {
    @NonNull
    private Long eventID;
    @NonNull
    private String content;
    @NonNull
    private String username;
    @NonNull
    private LocalDateTime dateTime;
}
