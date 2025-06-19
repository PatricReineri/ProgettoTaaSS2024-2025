package com.service.galleryservice.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NonNull;

@Data
public class AddNewImageRequestDTO {
    @NonNull
    private Long eventID;

    @NonNull
    private String title;

    @NonNull
    private String base64Image;

    @NonNull
    private String uploadedBy; // username of sender

    private LocalDateTime dateTime;
}
