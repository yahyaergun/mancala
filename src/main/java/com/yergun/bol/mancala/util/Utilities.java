package com.yergun.bol.mancala.util;

public interface Utilities {
    static boolean isBetween(Integer position, Integer from, Integer to) {
        return position >= from && position <= to;
    }
}
