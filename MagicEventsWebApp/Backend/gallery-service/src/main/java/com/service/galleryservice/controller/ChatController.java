package com.service.galleryservice.controller;

import com.service.galleryservice.dto.AddNewImageRequestDTO;
import com.service.galleryservice.dto.DeleteImageRequestDTO;
import com.service.galleryservice.dto.ImageLikeRequestDTO;
import com.service.galleryservice.service.ImageChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ImageChatService imageChatService;

    @MessageMapping("gallery/sendImage/{eventID}")
    @SendTo("/topic/gallery/{eventID}")
    public AddNewImageRequestDTO receiveImage(@Payload AddNewImageRequestDTO message) {
        log.info("Received image message: {}", message);
        AddNewImageRequestDTO response = imageChatService.addNewImage(message);
        return response;
    }


    @MessageMapping("gallery/deleteImage/{eventID}")
    @SendTo("/topic/gallery/deleteImage/{eventID}")
    public DeleteImageRequestDTO deleteImage(@Payload DeleteImageRequestDTO msg) {
        log.info("Received delete request: {}", msg);
        return imageChatService.deleteImage(msg);
    }

    @MessageMapping("gallery/imageLike/{eventID}")
    @SendTo("/topic/gallery/imageLike/{eventID}")
    public ImageLikeRequestDTO handleImageLike(@Payload ImageLikeRequestDTO request) {
        log.info("Processing image like/unlike: {}", request);
        return imageChatService.handleImageLike(request);
    }


}
