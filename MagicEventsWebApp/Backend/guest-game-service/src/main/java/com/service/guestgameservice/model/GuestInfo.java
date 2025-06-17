package com.service.guestgameservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "guest_info", uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_magic_events_tag")
})
@Data
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
}
