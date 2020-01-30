package com.yergun.bol.mancala.service;

import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player getOrCreatePlayer(String name) {
        return playerRepository
                .findByName(name)
                .orElseGet(() -> playerRepository.save(new Player(name)));
    }
}
