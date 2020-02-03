package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.exception.PlayerNotLoggedInException;
import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.MoveRequest;
import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.service.GameService;
import com.yergun.bol.mancala.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public List<Game> gameList() {
        return gameService.getGameList();
    }

    @PostMapping
    public Game createGame(HttpSession session) {
        Player player = getPlayerFromSession(session)
                .orElseThrow(() -> new PlayerNotLoggedInException("You have to login to create a game!"));
        Game game = gameService.createGame(player);
        messagingTemplate.convertAndSend(Constants.UPDATE_GAMELIST_WS_TOPIC, gameService.getGameList());
        return game;
    }

    @PostMapping("/{id}/join")
    public Game joinGame(@PathVariable Long id, HttpSession session) {
        Player player = getPlayerFromSession(session)
                .orElseThrow(() -> new PlayerNotLoggedInException("You have to login to join a game!"));
        Game game = gameService.joinGame(id, player);
        messagingTemplate.convertAndSend(Constants.UPDATE_GAMELIST_WS_TOPIC, gameService.getGameList());
        messagingTemplate.convertAndSend(Constants.UPDATE_GAME_WS_TOPIC, game);
        return game;
    }

    @PostMapping("/{id}/move")
    public Game makeMove(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
        Game game = gameService.makeAMove(id, moveRequest);
        messagingTemplate.convertAndSend(Constants.UPDATE_GAME_WS_TOPIC, game);
        return game;
    }

    private Optional<Player> getPlayerFromSession(HttpSession session) {
        return Optional.ofNullable((Player) session.getAttribute(Constants.SESSION_PLAYER_IDENTIFIER));
    }
}
