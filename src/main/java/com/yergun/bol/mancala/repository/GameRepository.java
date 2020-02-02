package com.yergun.bol.mancala.repository;

import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByStateIsIn(List<GameState> states);
}
