package com.yergun.bol.mancala.model;

import com.yergun.bol.mancala.initializer.GameInitializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void getNextPit_fromPositionZero_ShouldReturnPositionOne() {
        Board board = GameInitializer.initializeGame().getBoard();

        Pit nextPit = board.getNextPit(board.getPits().get(0));

        assertThat(nextPit.getPosition()).isEqualTo(1);
    }

    @Test
    void getNextPit_fromPositionThirteen_ShouldReturnPositionZero() {
        Board board = GameInitializer.initializeGame().getBoard();

        Pit nextPit = board.getNextPit(board.getPits().get(13));

        assertThat(nextPit.getPosition()).isEqualTo(0);
    }

    @Test
    void getOppositePit_fromPositionZero_shouldReturnPositionEleven() {
        Board board = GameInitializer.initializeGame().getBoard();

        Pit oppositePit = board.getOppositePit(board.getPits().get(1));

        assertThat(oppositePit.getPosition()).isEqualTo(11);
    }

    @Test
    void getPlayerOneMancala_shouldReturnPitWithTypePlayerOneMancala() {
        Board board = GameInitializer.initializeGame().getBoard();

        Pit playerOneMancalaPit = board.getPlayerOneMancala();

        assertThat(playerOneMancalaPit.getPitType()).isEqualByComparingTo(PitType.PLAYER_ONE_MANCALA);
    }

    @Test
    void getPlayerTwoMancala_shouldReturnPitWithTypePlayerTwoMancala() {
        Board board = GameInitializer.initializeGame().getBoard();

        Pit playerTwoMancalaPit = board.getPlayerTwoMancala();

        assertThat(playerTwoMancalaPit.getPitType()).isEqualByComparingTo(PitType.PLAYER_TWO_MANCALA);
    }

    @Test
    void isAPlayerPitsEmpty_whenPitsAreNotEmpty_shouldReturnFalse() {
        Board board = GameInitializer.initializeGame().getBoard();

        assertThat(board.isAPlayersPitsEmpty()).isEqualTo(false);
    }

    @Test
    void isAPlayerPitsEmpty_whenEmptyPits_shouldReturnTrue() {
        Board board = GameInitializer.initializeGame().getBoard();
        board.getPits().forEach(p -> p.setMarbleCount(0));

        assertThat(board.isAPlayersPitsEmpty()).isEqualTo(true);
    }

    @Test
    void copyRemainingPitsToMancala() {
        Board board = GameInitializer.initializeGame().getBoard();

        board.copyRemainingMarblesToMancalaPits();

        assertThat(board.isAPlayersPitsEmpty()).isEqualTo(true);
        assertThat(board.getPlayerOneMancala().getMarbleCount()).isEqualTo(36);
        assertThat(board.getPlayerTwoMancala().getMarbleCount()).isEqualTo(36);
    }

}
