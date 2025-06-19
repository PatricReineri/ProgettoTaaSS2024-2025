package com.service.galleryservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ImageDTO {
    private Long id;
    private String title;
    private String base64Image;
    private LocalDateTime dateTime;
    private String uploadedBy;
    private List<ImageUserLikeDTO> imageUserLikes;
}
