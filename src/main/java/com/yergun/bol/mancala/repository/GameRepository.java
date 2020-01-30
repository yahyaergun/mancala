package com.yergun.bol.mancala.repository;

import com.yergun.bol.mancala.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameRepository extends JpaRepository<Game, Long> {
}
