package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.exception.PlayerNotLoggedInException;
import com.yergun.bol.mancala.model.Game;
import com.yergun.bol.mancala.model.MoveRequest;
import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.yergun.bol.mancala.util.Constants.SESSION_PLAYER_IDENTIFIER;
import static com.yergun.bol.mancala.util.Constants.UPDATE_GAMELIST_WS_TOPIC;
import static com.yergun.bol.mancala.util.Constants.UPDATE_GAME_WS_TOPIC;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = GameController.class)
class GameControllerTest {

    private static final String BASE_GAMES_URL = "/games";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    void gameList_whenGet_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_GAMES_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createGame_whenPostWithValidPlayerSession_thenStatus200() throws Exception {
        mockMvc.perform(post(BASE_GAMES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr(SESSION_PLAYER_IDENTIFIER, new Player()))
                .andExpect(status().isOk());
    }

    @Test
    void createGame_whenPostWithNoPlayerSession_thenStatus500AndException() {
        assertThatThrownBy(() -> mockMvc.perform(post(BASE_GAMES_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()))
                .hasCauseInstanceOf(PlayerNotLoggedInException.class)
                .hasMessageContaining("You have to login to create a game");
    }

    @Test
    void joinGame_whenPostWithValidPlayerSession_thenStatus200andWebsocketMessageIsSent() throws Exception {
        long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);
        Player player = new Player("player1");
        when(gameService.joinGame(1L, player)).thenReturn(game);
        when(gameService.getGameList()).thenReturn(List.of(game));

        mockMvc.perform(post(BASE_GAMES_URL + "/"+ gameId +"/join")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr(SESSION_PLAYER_IDENTIFIER, player))
                .andExpect(status().isOk());

        verify(messagingTemplate, times(1)).convertAndSend(UPDATE_GAMELIST_WS_TOPIC, List.of(game));
    }

    @Test
    void makeMove_whenPost_returns200AndWebsocketMessageIsSent() throws Exception {
        long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);
        when(gameService.makeAMove(1L, new MoveRequest(1))).thenReturn(game);

        mockMvc.perform(post(BASE_GAMES_URL + "/"+ gameId +"/move")
                .content("{ \"position\": 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(messagingTemplate, times(1)).convertAndSend(UPDATE_GAME_WS_TOPIC, game);
    }


}
