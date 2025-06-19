package com.service.galleryservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class GalleryDTO {
    private Long eventID;
    private String title;
    private List<ImageDTO> images;
}

