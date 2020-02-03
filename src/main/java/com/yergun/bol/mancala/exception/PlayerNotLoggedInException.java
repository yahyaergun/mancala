package com.yergun.bol.mancala.exception;

public class PlayerNotLoggedInException extends RuntimeException {
    public PlayerNotLoggedInException(String message) {
        super(message);
    }
}
