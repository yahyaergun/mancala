package com.yergun.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Board board;
    private Turn turn;

    @OneToOne
    private Player player1;

    @OneToOne
    private Player player2;

    @Embedded
    private Score score;
    private GameState state;

    public void toggleTurn() {
        turn = (turn == Turn.PLAYER_ONE ? Turn.PLAYER_TWO : Turn.PLAYER_ONE);
    }

    public void updateScore() {
        this.score.setPlayerOneScore(this.board.getPlayerOneMancala().getMarbleCount());
        this.score.setPlayerTwoScore(this.board.getPlayerTwoMancala().getMarbleCount());
    }

    public void updateGameState() {
        if (this.board.isAPlayersPitsEmpty()) {
            this.board.copyRemainingMarblesToMancalaPits();
            this.setState(GameState.ENDED);
        }
    }
}
