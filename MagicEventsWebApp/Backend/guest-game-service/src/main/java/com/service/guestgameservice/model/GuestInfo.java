package com.service.guestgameservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "guest_info", uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_magic_events_tag")
})
public class GuestInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_men", nullable = false)
    private Boolean isMen;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "is_host_family_member", nullable = false)
    private Boolean isHostFamilyMember;

    @Column(name = "is_host_associate", nullable = false)
    private Boolean isHostAssociate;

    @Column(name = "have_beard", nullable = false)
    private Boolean haveBeard;

    @Column(name = "is_bald", nullable = false)
    private Boolean isBald;

    @Column(name = "have_glasses", nullable = false)
    private Boolean haveGlasses;

    @Column(name = "have_dark_hair", nullable = false)
    private Boolean haveDarkHair;

    @Column(name = "user_magic_events_tag", nullable = false, unique = true)
    private String userMagicEventsTag;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public GuestInfo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
}
