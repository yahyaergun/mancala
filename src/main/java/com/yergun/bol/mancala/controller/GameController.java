package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.MoveRequest;
import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.service.GameService;
import com.yergun.bol.mancala.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@CrossOrigin(allowCredentials = "true",
        allowedHeaders = {"Origin", "Content-Type", "Accept"},
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
public class GameController {

    private final GameService gameService;

    @GetMapping
    public List<Game> gameList() {
        return gameService.getGameList();
    }

    @PostMapping
    public Game createGame(HttpSession session) {
        Player player = getPlayerFromSession(session)
                .orElseThrow(() -> new RuntimeException("You have to login to create a game!"));
        return gameService.createGame(player);
    }

    @PostMapping("/{id}/join")
    public Game joinGame(@PathVariable Long id, HttpSession session) {
        Player player = getPlayerFromSession(session)
                .orElseThrow(() -> new RuntimeException("You have to login to join a game!"));
        return gameService.joinGame(id, player);
    }

    @PostMapping("/{id}/move")
    public Game makeMove(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
        return gameService.makeAMove(id, moveRequest);
    }

    private Optional<Player> getPlayerFromSession(HttpSession session) {
        return Optional.ofNullable((Player) session.getAttribute(Constants.SESSION_PLAYER_IDENTIFIER));
    }
}
