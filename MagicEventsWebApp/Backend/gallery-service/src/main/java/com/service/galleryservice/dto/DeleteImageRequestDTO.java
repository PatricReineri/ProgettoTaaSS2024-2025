package com.service.galleryservice.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.Builder;

@Data
public class DeleteImageRequestDTO {
    @NonNull
    private Long eventID;

    @NonNull
    private Long imageID;

    @NonNull
    private String deletedBy;
}
