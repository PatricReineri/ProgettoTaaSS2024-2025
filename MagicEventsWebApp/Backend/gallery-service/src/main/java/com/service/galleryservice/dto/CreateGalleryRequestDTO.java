package com.service.galleryservice.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateGalleryRequestDTO {
    @NonNull
    private Long eventID;

    @NonNull
    private String title;
}

