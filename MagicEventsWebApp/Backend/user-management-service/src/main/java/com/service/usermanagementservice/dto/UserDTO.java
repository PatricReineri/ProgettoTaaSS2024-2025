package com.service.usermanagementservice.dto;

public class UserDTO {
    private Long magicEventTag;
    private String username;
    private String email;
    private String profileImageUrl;
    private String name;
    private String surname;
    private String role;

    public UserDTO(
            Long magicEventTag,
            String username,
            String email,
            String profileImageUrl,
            String name,
            String surname,
            String role
    ) {
        this.magicEventTag = magicEventTag;
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public Long getMagicEventTag() {
        return magicEventTag;
    }

    public void setMagicEventTag(Long magicEventTag) {
        this.magicEventTag = magicEventTag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

