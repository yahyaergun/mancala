package com.yergun.bol.mancala.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void isBetween_whenCalledForPositionTwoFromOneToThree_shouldReturnTrue() {
        boolean isBetween = Utilities.isBetween(2, 1, 3);

        assertThat(isBetween).isEqualTo(true);
    }

    @Test
    void isBetween_whenCalledForPositionTwoFromThreeToFive_shouldReturnFalse() {
        boolean isBetween = Utilities.isBetween(2, 3, 5);

        assertThat(isBetween).isEqualTo(false);
    }
}
