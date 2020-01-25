package com.yergun.bol.mancala.model;

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
}
