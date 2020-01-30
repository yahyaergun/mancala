package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.service.PlayerService;
import com.yergun.bol.mancala.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/players")
@CrossOrigin(allowCredentials = "true",
        allowedHeaders = {"Origin", "Content-Type", "Accept"},
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public Player login(@RequestBody String name, HttpSession session) {
        Player player = playerService.getOrCreatePlayer(name);
        session.setAttribute(Constants.SESSION_PLAYER_IDENTIFIER, player);
        return player;
    }

}
