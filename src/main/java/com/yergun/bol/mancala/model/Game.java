package com.yergun.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Board board;
    private Turn turn = Turn.PLAYER_ONE;

    private String player1;
    private String player2;
    @Embedded
    private Score score;

    public void toggleTurn() {
        turn = (turn == Turn.PLAYER_ONE ? Turn.PLAYER_TWO : Turn.PLAYER_ONE);
    }

    public void updateScore() {
        //get score from each players' mancala cup
    }
}
