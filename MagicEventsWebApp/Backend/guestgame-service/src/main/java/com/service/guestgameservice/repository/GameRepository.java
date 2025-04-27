package com.service.guestgameservice.repository;
import com.service.guestgameservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameRepository extends JpaRepository<Game, Long> {
}
