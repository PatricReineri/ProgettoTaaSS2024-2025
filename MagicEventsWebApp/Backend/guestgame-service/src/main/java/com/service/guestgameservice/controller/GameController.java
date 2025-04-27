package com.service.guestgameservice.controller;
import com.service.guestgameservice.model.Game;
import com.service.guestgameservice.repository.GameRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("GameStatus")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @ModelAttribute("GameStatus")
    public Game getOrCreateCart(HttpSession session) {
        Game game = (Game) session.getAttribute("gameStatus");
        if (game == null) {
            game = new Game("Akinator", 0);
            session.setAttribute("GameStatus", game);
        }
        return game;
    }

}
