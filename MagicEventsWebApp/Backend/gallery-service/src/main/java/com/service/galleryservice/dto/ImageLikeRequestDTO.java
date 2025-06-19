package com.service.galleryservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Data
public class ImageLikeRequestDTO {
    @NonNull
    private String userMagicEventsTag;
    private boolean like;
    @NonNull
    private Long imageID;

    private int likedCount = 0;
}
