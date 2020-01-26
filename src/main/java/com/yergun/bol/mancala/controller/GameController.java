package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.MoveRequest;
import com.yergun.bol.mancala.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
@CrossOrigin
public class GameController {

    private final GameService gameService;

    @PostMapping
    public Game createGame() {
        return gameService.createGame();
    }

    @PostMapping("/{id}/move")
    public Game makeMove(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
        return gameService.makeAMove(id, moveRequest);
    }
}
