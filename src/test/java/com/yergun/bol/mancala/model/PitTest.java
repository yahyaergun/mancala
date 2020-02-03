package com.yergun.bol.mancala.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PitTest {

    @Test
    void incrementMarbles_shouldIncrementMarbleCount() {
        Pit pit = new Pit();
        pit.setMarbleCount(1);

        pit.incrementMarbles();

        assertThat(pit.getMarbleCount()).isEqualTo(2);
    }

    @Test
    void addMarbles_whenAddedTwoOnOne_shouldResultInThree() {
        Pit pit = new Pit();
        pit.setMarbleCount(1);

        pit.addMarbles(2);

        assertThat(pit.getMarbleCount()).isEqualTo(3);
    }

    @Test
    void collectMarbles_shouldSetCountToZeroAndReturnOriginalCount() {
        Pit pit = new Pit();
        pit.setMarbleCount(5);

        int grabbedMarbleCount = pit.collectMarbles();

        assertThat(pit.getMarbleCount()).isEqualTo(0);
        assertThat(grabbedMarbleCount).isEqualTo(5);
    }
}
