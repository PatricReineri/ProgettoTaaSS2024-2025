package com.service.boardservice.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardMessageDTO {
    private Long messageID;
    private String content;
    private String username;
    private LocalDateTime time;

    /** Public constructor to allow access from other packages */
    public BoardMessageDTO(
            @NonNull Long messageID,
            @NonNull String content,
            @NonNull String username,
            @NonNull LocalDateTime time
    ) {
        this.messageID = messageID;
        this.content = content;
        this.username = username;
        this.time = time;
    }
}