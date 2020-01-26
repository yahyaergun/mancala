package com.yergun.bol.mancala.service;

import com.yergun.bol.mancala.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GameInitializer {

    public static Game initializeGame() {
        return Game.builder()
                .state(GameState.IN_PROGRESS)
                .board(initializeBoard())
                .player1("Player1")
                .player2("Player2")
                .turn(Turn.PLAYER_ONE)
                .score(new Score(0, 0))
                .build();
    }

    private static Board initializeBoard() {
        Board board = new Board();
        board.setPits(initializePits(board));
        return board;
    }

    private static List<Pit> initializePits(Board board) {
        List<Pit> pits = new ArrayList<>(createStandardPitsBetween(board, 0, 5));
        pits.add(createMancalaPit(board, PitType.PLAYER_ONE_MANCALA, 6));
        pits.addAll(createStandardPitsBetween(board, 7, 12));
        pits.add(createMancalaPit(board, PitType.PLAYER_TWO_MANCALA, 13));
        return pits;
    }

    private static List<Pit> createStandardPitsBetween(Board board, int from, int to) {
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

    private static Pit createMancalaPit(Board board, PitType pitType, Integer position) {
        return Pit.builder()
                .position(position)
                .pitType(pitType)
                .marbleCount(0)
                .board(board)
                .build();
    }

}
