package com.yergun.bol.mancala.model;

import com.yergun.bol.mancala.initializer.GameInitializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class GameTest {

    @Test
    void toggleTurn_whenPlayerOne_TogglesTurnToPlayerTwo() {
        Game game = Game.builder().turn(Turn.PLAYER_ONE).build();

        game.toggleTurn();

        assertThat(game.getTurn()).isEqualByComparingTo(Turn.PLAYER_TWO);
    }

    @Test
    void updateScore_whenCalled_copiesCountsFromMancalaPitToScore() {
        final Integer playerOneMarbleCount = 13;
        final Integer playerTwoMarbleCount = 66;

        Game game = GameInitializer.initializeGame();
        game.getBoard().getPlayerOneMancala().setMarbleCount(playerOneMarbleCount);
        game.getBoard().getPlayerTwoMancala().setMarbleCount(playerTwoMarbleCount);

        game.updateScore();

        assertThat(game.getScore().getPlayerOneScore()).isEqualTo(playerOneMarbleCount);
        assertThat(game.getScore().getPlayerTwoScore()).isEqualTo(playerTwoMarbleCount);
    }

    @Test
    void updateState_whenCalled_endsTheGameIfAPlayersAllPitAreEmpty() {
        Game game = GameInitializer.initializeGame();
        game.getBoard().getPits().forEach(p -> p.setMarbleCount(0));

        game.updateGameState();

        assertThat(game.getState()).isEqualByComparingTo(GameState.ENDED);
    }

}
