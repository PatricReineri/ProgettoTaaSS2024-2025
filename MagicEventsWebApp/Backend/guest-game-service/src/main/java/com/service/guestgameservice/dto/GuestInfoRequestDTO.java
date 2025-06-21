package com.service.guestgameservice.dto;

import jakarta.validation.constraints.*;
public class GuestInfoRequestDTO {
    @NotNull(message = "Gender information is required")
    private Boolean isMen;
    
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 130, message = "Age must be at most 130")
    private Integer age;
    
    @NotNull(message = "Host family member status is required")
    private Boolean isHostFamilyMember;
    
    @NotNull(message = "Host associate status is required")
    private Boolean isHostAssociate;
    
    @NotNull(message = "Beard information is required")
    private Boolean haveBeard;
    
    @NotNull(message = "Baldness information is required")
    private Boolean isBald;
    
    @NotNull(message = "Glasses information is required")
    private Boolean haveGlasses;
    
    @NotNull(message = "Hair color information is required")
    private Boolean haveDarkHair;
    
    @NotBlank(message = "User Magic Events Tag is required")
    private String userMagicEventsTag;
    
    @NotNull(message = "Game ID is required")
    @Positive(message = "Game ID must be positive")
    private Long gameId;

    public GuestInfoRequestDTO() {}

    public Boolean getIsMen() { return isMen; }
    public void setIsMen(Boolean isMen) { this.isMen = isMen; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Boolean getIsHostFamilyMember() { return isHostFamilyMember; }
    public void setIsHostFamilyMember(Boolean isHostFamilyMember) { this.isHostFamilyMember = isHostFamilyMember; }

    public Boolean getIsHostAssociate() { return isHostAssociate; }
    public void setIsHostAssociate(Boolean isHostAssociate) { this.isHostAssociate = isHostAssociate; }

    public Boolean getHaveBeard() { return haveBeard; }
    public void setHaveBeard(Boolean haveBeard) { this.haveBeard = haveBeard; }

    public Boolean getIsBald() { return isBald; }
    public void setIsBald(Boolean isBald) { this.isBald = isBald; }

    public Boolean getHaveGlasses() { return haveGlasses; }
    public void setHaveGlasses(Boolean haveGlasses) { this.haveGlasses = haveGlasses; }

    public Boolean getHaveDarkHair() { return haveDarkHair; }
    public void setHaveDarkHair(Boolean haveDarkHair) { this.haveDarkHair = haveDarkHair; }

    public String getUserMagicEventsTag() { return userMagicEventsTag; }
    public void setUserMagicEventsTag(String userMagicEventsTag) { this.userMagicEventsTag = userMagicEventsTag; }

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
}
