package com.service.guestgameservice.dto;

public class GuestInfoRequestDTO {
    private Boolean isMen;
    private Integer age;
    private Boolean isHostFamilyMember;
    private Boolean isHostAssociate;
    private Boolean haveBeard;
    private Boolean isBald;
    private Boolean haveGlasses;
    private Boolean haveDarkHair;
    private String userMagicEventsTag;
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
