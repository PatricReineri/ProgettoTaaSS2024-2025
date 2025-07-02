package com.service.guestgameservice.repository;

import com.service.guestgameservice.model.Game;
import com.service.guestgameservice.model.GuestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestGameRepository extends JpaRepository<GuestInfo, Long> {
    List<GuestInfo> findByGame(Game game);

    GuestInfo findByGameAndUserMagicEventsTag(Game game, String userMagicEventsTag);
}
