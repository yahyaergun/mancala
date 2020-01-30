package com.yergun.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Player {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    public Integer gamesWon;
    public Integer gamesLost;

    public Player(String name) {
        this.name = name;
        gamesLost = 0;
        gamesWon = 0;
    }
}
