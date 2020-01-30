package com.yergun.bol.mancala.repository;

import com.yergun.bol.mancala.model.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> findByName(String name);
}
