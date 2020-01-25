package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @PostMapping
    public Game createGame() {
        return gameService.createGame();
    }
}
