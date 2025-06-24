package com.service.galleryservice.controller;

import com.service.galleryservice.dto.AddNewImageRequestDTO;
import com.service.galleryservice.dto.DeleteImageRequestDTO;
import com.service.galleryservice.dto.ImageLikeRequestDTO;
import com.service.galleryservice.exception.UnauthorizedException;
import com.service.galleryservice.service.ImageChatService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ChatController {
    private final ImageChatService imageChatService;

    public ChatController(ImageChatService imageChatService) {
        this.imageChatService = imageChatService;
    }

    @MessageMapping("gallery/sendImage/{eventID}")
    @SendTo("/topic/gallery/{eventID}")
    public AddNewImageRequestDTO receiveImage(@Valid @Payload AddNewImageRequestDTO message) {
        try {
            AddNewImageRequestDTO response = imageChatService.addNewImage(message);
            return response;
        } catch (UnauthorizedException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @MessageMapping("gallery/deleteImage/{eventID}")
    @SendTo("/topic/gallery/deleteImage/{eventID}")
    public DeleteImageRequestDTO deleteImage(@Valid @Payload DeleteImageRequestDTO msg) {
        try {
            return imageChatService.deleteImage(msg);
        } catch (UnauthorizedException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @MessageMapping("gallery/imageLike/{eventID}")
    @SendTo("/topic/gallery/imageLike/{eventID}")
    public ImageLikeRequestDTO handleImageLike(@Valid @Payload ImageLikeRequestDTO request) {
        try {
            return imageChatService.handleImageLike(request);
        } catch (UnauthorizedException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
