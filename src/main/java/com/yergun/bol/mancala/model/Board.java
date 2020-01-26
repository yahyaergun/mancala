package com.yergun.bol.mancala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Board {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Pit> pits;

    public Pit getNextPit(Pit from) {
        int nextPitPosition = (pits.indexOf(from) + 1) % 14; // for circling back from position 13 -> 0
        return pits.get(nextPitPosition);
    }

    public Pit getOppositePit(Pit from) {
        int oppositePosition = pits.size() - (from.getPosition() + 2);
        return pits.get(oppositePosition);
    }

    @JsonIgnore
    public Pit getPlayerOneMancala() {
        return pits.get(6);
    }

    @JsonIgnore
    public Pit getPlayerTwoMancala() {
        return pits.get(13);
    }

    @JsonIgnore
    public boolean isAPlayersPitsEmpty() {
        return isPlayerOnePitsEmpty() || isPlayerTwoPitsEmpty();
    }

    private boolean isPlayerOnePitsEmpty() {
        return getPlayerOneStandardPits()
                .stream()
                .noneMatch(p -> p.getMarbleCount() > 0);
    }

    private boolean isPlayerTwoPitsEmpty() {
        return getPlayerTwoStandardPits()
                .stream()
                .noneMatch(p -> p.getMarbleCount() > 0);
    }


    private List<Pit> getPlayerOneStandardPits() {
        return pits.subList(0, 6);
    }

    private List<Pit> getPlayerTwoStandardPits() {
        return pits.subList(7, 13);
    }
}
