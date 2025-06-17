package com.service.guestgameservice.repository;

import com.service.guestgameservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByEventId(Long eventId);
}
