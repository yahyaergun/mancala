package com.yergun.bol.mancala.service;

import com.yergun.bol.mancala.model.*;
import com.yergun.bol.mancala.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Log4j2
@Service
public class GameService {

    private final GameRepository gameRepository;

    public Game createGame() {
        Game game = new Game();
        game.setBoard(initializeBoard());
        game.setPlayer1("Player1");
        game.setPlayer2("Player2");
        game.setScore(new Score(0, 0));
        gameRepository.save(game);
        return game;
    }

    private Board initializeBoard() {
        Board board = new Board();
        board.setPits(initializePits(board));
        return board;
    }

    private List<Pit> initializePits(Board board) {
        List<Pit> pits = new ArrayList<>(createStandardPitsBetween(board, 0, 5));
        pits.add(createMancalaPit(board, 6));
        pits.addAll(createStandardPitsBetween(board, 7, 12));
        pits.add(createMancalaPit(board, 13));
        return pits;
    }

    private List<Pit> createStandardPitsBetween(Board board, int from, int to) {
        List<Pit> pits = new ArrayList<>();
        IntStream.rangeClosed(from, to)
                .forEach(i -> {
                    Pit pit = Pit.builder()
                            .pitType(PitType.STANDARD)
                            .position(i)
                            .marbleCount(6)
                            .board(board)
                            .build();
                    pits.add(pit);
                });
        return pits;
    }

    private Pit createMancalaPit(Board board, Integer position) {
        return Pit.builder()
                .position(position)
                .pitType(PitType.MANCALA)
                .marbleCount(0)
                .board(board)
                .build();
    }
}
