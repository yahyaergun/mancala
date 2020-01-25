package com.yergun.bol.mancala.repository;

import com.yergun.bol.mancala.model.Game;
import org.springframework.data.repository.CrudRepository;


public interface GameRepository extends CrudRepository<Game, Long> {
}
