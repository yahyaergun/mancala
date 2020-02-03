package com.yergun.bol.mancala.controller;

import com.yergun.bol.mancala.model.Player;
import com.yergun.bol.mancala.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.yergun.bol.mancala.util.Constants.SESSION_PLAYER_IDENTIFIER;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
class PlayerControllerTest {

    private static final String BASE_PLAYERS_URL = "/players";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    void getCurrentPlayer_whenCalledWithValidSession_Returns200 () throws Exception {
        mockMvc.perform(get(BASE_PLAYERS_URL + "/current")
                .sessionAttr(SESSION_PLAYER_IDENTIFIER, new Player())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void login_whenCalledWithName_Returns200() throws Exception {
        String name = "playerName";
        Player player = new Player(name);

        when(playerService.getOrCreatePlayer(name)).thenReturn(player);

        mockMvc.perform(post(BASE_PLAYERS_URL)
                .content(name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(name)))
                .andExpect(status().isOk());
    }

    @Test
    void logout_whenCalledWithPlayerInSession_RemovesSessionAttrAndReturns200() throws Exception {
        mockMvc.perform(post(BASE_PLAYERS_URL + "/logout")
                .sessionAttr(SESSION_PLAYER_IDENTIFIER, new Player("playerName"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
