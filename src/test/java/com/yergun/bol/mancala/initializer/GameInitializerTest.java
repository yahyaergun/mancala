package com.yergun.bol.mancala.initializer;

import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.GameState;
import com.yergun.bol.mancala.model.Score;
import com.yergun.bol.mancala.model.Turn;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class GameInitializerTest {

    @Test
    void initializeGame_whenCalled_returnsAValidGameAtStartingPoint() {
        int PIT_COUNT = 14;

        Game game = GameInitializer.initializeGame();

        assertThat(game.getState()).isEqualTo(GameState.WAITING_FOR_PLAYERS);
        assertThat(game.getBoard().getPits().size()).isEqualTo(PIT_COUNT);
        assertThat(game.getTurn()).isEqualByComparingTo(Turn.PLAYER_ONE);
        assertThat(game.getPlayer1()).isNull();
        assertThat(game.getPlayer2()).isNull();
        assertThat(game.getScore()).isEqualToComparingFieldByField(new Score(0, 0));
    }

}
