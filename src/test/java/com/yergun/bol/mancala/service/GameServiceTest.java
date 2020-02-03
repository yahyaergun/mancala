package com.yergun.bol.mancala.service;

import com.yergun.bol.mancala.exception.GameNotFoundException;
import com.yergun.bol.mancala.initializer.GameInitializer;
import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.GameState;
import com.yergun.bol.mancala.model.MoveRequest;
import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.model.Turn;
import com.yergun.bol.mancala.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void getGameList_whenCalled_returnsGames() {
        when(gameRepository.findAllByStateIsIn(List.of(GameState.WAITING_FOR_PLAYERS, GameState.IN_PROGRESS)))
                .thenReturn(List.of(new Game(), new Game()));

        List<Game> gameList = gameService.getGameList();

        assertThat(gameList.size()).isEqualTo(2);
    }

    @Test
    void createGame_whenCalledWithPlayer_initsGameAndSetsPlayer() {
        when(gameRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        String name = "player1";

        Game game = gameService.createGame(new Player(name));

        assertThat(game.getPlayer1().getName()).isEqualTo(name);
        assertThat(game.getState()).isEqualByComparingTo(GameState.WAITING_FOR_PLAYERS);
        assertThat(game.getBoard().getPits().size()).isEqualTo(14);
        assertThat(game.getTurn()).isEqualByComparingTo(Turn.PLAYER_ONE);
    }

    @Test
    void joinGame_whenCalledWithValidIdAndPlayer_setsPlayerAsPlayerTwoAndReturnsGame() {
        Game game = Game.builder().id(1L).build();
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        String name = "player1";
        Game result = gameService.joinGame(1L, new Player(name));

        assertThat(result.getPlayer2().getName()).isEqualTo(name);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void joinGame_whenCalledWithInvalidGameId_throwsGameNotFoundException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GameNotFoundException.class).isThrownBy(() -> gameService.joinGame(1L, new Player()));
    }

    @Test
    void makeMove_shouldSkipOppositionMancala() {
        Game game = GameInitializer.initializeGame();
        game.setId(1L);
        game.getBoard().getPits().get(5).setMarbleCount(8);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game gameAfterMove = gameService.makeAMove(1L, new MoveRequest(5));

        assertThat(gameAfterMove.getBoard().getPits().get(5).getMarbleCount()).isEqualTo(0);
        assertThat(gameAfterMove.getBoard().getPlayerTwoMancala().getMarbleCount()).isEqualTo(0);
        assertThat(gameAfterMove.getTurn()).isEqualByComparingTo(Turn.PLAYER_TWO);
    }

    @Test
    void makeMove_shouldCollectOwnAndOppositionPitIfLastMarbleLandsOnOwnEmptyPit() {
        Game game = GameInitializer.initializeGame();
        game.setId(1L);
        game.getBoard().getPits().get(1).setMarbleCount(0);
        game.getBoard().getPits().get(0).setMarbleCount(1);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game gameAfterMove = gameService.makeAMove(1L, new MoveRequest(0));

        assertThat(gameAfterMove.getBoard().getPits().get(0).getMarbleCount()).isEqualTo(0);
        assertThat(gameAfterMove.getBoard().getPlayerOneMancala().getMarbleCount()).isEqualTo(7); // 1 from own pit, +6 from opposition pit
        assertThat(gameAfterMove.getTurn()).isEqualByComparingTo(Turn.PLAYER_TWO);
    }

    @Test
    void makeMove_shouldEndGameIfAPlayersHasEveryPitEmpty() {
        Game game = GameInitializer.initializeGame();
        game.setId(1L);
        game.getBoard().getPits().get(0).setMarbleCount(0);
        game.getBoard().getPits().get(1).setMarbleCount(0);
        game.getBoard().getPits().get(2).setMarbleCount(0);
        game.getBoard().getPits().get(3).setMarbleCount(0);
        game.getBoard().getPits().get(4).setMarbleCount(0);
        game.getBoard().getPits().get(5).setMarbleCount(1); // Move on position 5 should finish the game.
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game gameAfterMove = gameService.makeAMove(1L, new MoveRequest(5));

        assertThat(gameAfterMove.getBoard().getPits().get(5).getMarbleCount()).isEqualTo(0);
        assertThat(gameAfterMove.getState()).isEqualByComparingTo(GameState.ENDED);
    }

    @Test
    void makeMove_whenCalledWithValidGameIdAndMoveEndsWithMancala_returnsUpdatedGameWithSameTurnAndCorrectCounts() {
        int EXPECTED_COUNT_AFTER_A_SOW = 7; //starting from 6, +1 after move
        Game game = GameInitializer.initializeGame();
        game.setId(1L);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game gameAfterMove = gameService.makeAMove(1L, new MoveRequest(0));

        assertThat(gameAfterMove.getBoard().getPits().get(0).getMarbleCount()).isEqualTo(0);
        assertThat(gameAfterMove.getBoard().getPits().get(1).getMarbleCount()).isEqualTo(EXPECTED_COUNT_AFTER_A_SOW);
        assertThat(gameAfterMove.getBoard().getPits().get(2).getMarbleCount()).isEqualTo(EXPECTED_COUNT_AFTER_A_SOW);
        assertThat(gameAfterMove.getBoard().getPits().get(3).getMarbleCount()).isEqualTo(EXPECTED_COUNT_AFTER_A_SOW);
        assertThat(gameAfterMove.getBoard().getPits().get(4).getMarbleCount()).isEqualTo(EXPECTED_COUNT_AFTER_A_SOW);
        assertThat(gameAfterMove.getBoard().getPits().get(5).getMarbleCount()).isEqualTo(EXPECTED_COUNT_AFTER_A_SOW);
        assertThat(gameAfterMove.getBoard().getPlayerOneMancala().getMarbleCount()).isEqualTo(1);
        assertThat(gameAfterMove.getTurn()).isEqualByComparingTo(Turn.PLAYER_ONE);
        assertThat(gameAfterMove.getScore().getPlayerOneScore()).isEqualByComparingTo(1);
    }

    @Test
    void makeMove_whenCalledWithInvalidGameId_throwsGameNotFoundException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GameNotFoundException.class).isThrownBy(() -> gameService.makeAMove(1L, new MoveRequest(1)));
    }




}
