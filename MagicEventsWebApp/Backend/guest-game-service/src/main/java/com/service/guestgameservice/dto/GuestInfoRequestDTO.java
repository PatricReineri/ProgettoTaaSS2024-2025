package com.service.guestgameservice.dto;

import lombok.Data;

@Data
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
}
