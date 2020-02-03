package com.yergun.bol.mancala.service;

import com.yergun.bol.mancala.exception.GameNotFoundException;
import com.yergun.bol.mancala.initializer.GameInitializer;
import com.yergun.bol.mancala.model.*;
import com.yergun.bol.mancala.repository.GameRepository;
import com.yergun.bol.mancala.util.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class GameService {

    private final GameRepository gameRepository;

    public List<Game> getGameList() {
        return gameRepository.findAllByStateIsIn(List.of(GameState.WAITING_FOR_PLAYERS, GameState.IN_PROGRESS));
    }

    public Game createGame(Player player) {
        Game game = GameInitializer.initializeGame();
        game.setPlayer1(player);
        gameRepository.save(game);
        return game;
    }

    public Game joinGame(Long id, Player player) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("No game by id:[" + id + "]"));
        game.setPlayer2(player);
        game.setState(GameState.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }

    public Game makeAMove(Long id, MoveRequest moveRequest) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("No game by id:[" + id + "]"));
        return this.doMakeAMove(game, moveRequest.getPosition());
    }

    private Game doMakeAMove(Game game, Integer position) {
        List<Pit> pits = game.getBoard().getPits();
        Pit currentPit = pits.get(position);
        int marbleCount = currentPit.collectMarbles();
        Turn turn = game.getTurn();
        log.info("Turn in progress for {}", turn);
        while (marbleCount > 0) {
            Pit pit = game.getBoard().getNextPit(currentPit);

            if (isOppositionMancalaPit(turn, pit)) {
                currentPit = pit;
                log.info("Skipping opposition mancala pit");
                continue;
            }

            pit.incrementMarbles();
            log.info("Marble at pit position {} incremented to {}.", pit.getPosition(), pit.getMarbleCount());
            marbleCount--;
            currentPit = pit;
        }

        if (isPlayersPit(turn, currentPit) && hasOneMarble(currentPit)) {
            log.info("Last marble landed in player's own empty pit, collecting marbles from it and the opposite pit and adding it to player's mancala!");
            collectPitsToMancala(game, currentPit);
        }

        if (!isPlayersMancalaPit(turn, currentPit)) {
            log.info("Toggling turn to other player.");
            game.toggleTurn();
        }

        game.updateScore();
        game.updateGameState();

        gameRepository.save(game);
        return game;
    }

    private void collectPitsToMancala(Game game, Pit currentPit) {
        Board board = game.getBoard();
        Pit oppositePit = board.getOppositePit(currentPit);
        int ownMarbles = currentPit.collectMarbles();
        int oppositeMarbles = oppositePit.collectMarbles();

        if (game.getTurn() == Turn.PLAYER_ONE) {
            Pit playerOneMancala = board.getPlayerOneMancala();
            playerOneMancala.addMarbles(ownMarbles + oppositeMarbles);
        } else {
            Pit playerTwoMancala = board.getPlayerTwoMancala();
            playerTwoMancala.addMarbles(ownMarbles + oppositeMarbles);
        }

    }

    private boolean hasOneMarble(Pit pit) {
        return pit.getMarbleCount() == 1;
    }

    private boolean isPlayersPit(Turn turn, Pit pit) {
        return (turn == Turn.PLAYER_ONE && Utilities.isBetween(pit.getPosition(), 0, 5))
                || (turn == Turn.PLAYER_TWO && Utilities.isBetween(pit.getPosition(), 7, 12));
    }

    private boolean isPlayersMancalaPit(Turn turn, Pit pit) {
        return (turn == Turn.PLAYER_ONE && pit.getPitType() == PitType.PLAYER_ONE_MANCALA)
                || (turn == Turn.PLAYER_TWO && pit.getPitType() == PitType.PLAYER_TWO_MANCALA);
    }

    private boolean isOppositionMancalaPit(Turn turn, Pit pit) {
        return (turn == Turn.PLAYER_ONE && pit.getPitType() == PitType.PLAYER_TWO_MANCALA)
                || (turn == Turn.PLAYER_TWO && pit.getPitType() == PitType.PLAYER_ONE_MANCALA);
    }
}
