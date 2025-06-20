package com.service.galleryservice.dto;

import java.util.Objects;

public class ImageUserLikeDTO {
    private String userMagicEventsTag;

    public ImageUserLikeDTO() { }

    public ImageUserLikeDTO(String userMagicEventsTag) {
        this.userMagicEventsTag = userMagicEventsTag;
    }

    public String getUserMagicEventsTag() {
        return userMagicEventsTag;
    }

    public void setUserMagicEventsTag(String userMagicEventsTag) {
        this.userMagicEventsTag = userMagicEventsTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageUserLikeDTO that = (ImageUserLikeDTO) o;
        return Objects.equals(userMagicEventsTag, that.userMagicEventsTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userMagicEventsTag);
    }

    @Override
    public String toString() {
        return "ImageUserLikeDTO{" +
               "userMagicEventsTag='" + userMagicEventsTag + '\'' +
               '}';
    }
}

