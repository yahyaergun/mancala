package com.yergun.bol.mancala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Pit {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private Integer position;
    private Integer marbleCount;
    @Enumerated(EnumType.STRING)
    private PitType pitType;

    public void incrementMarbles() {
        marbleCount++;
    }

    public void addMarbles(int count) {
        marbleCount += count;
    }

    public int collectMarbles() {
        int grabbedMarbles = marbleCount;
        marbleCount = 0;
        return grabbedMarbles;
    }
}
