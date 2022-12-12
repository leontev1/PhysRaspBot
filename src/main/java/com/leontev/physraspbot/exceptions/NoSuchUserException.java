package com.leontev.physraspbot.exceptions;

public class NoSuchUserException extends Exception {
    private final NullGroupNumberException e = new NullGroupNumberException();

    @Override
    public String getMessage() {
        return e.getMessage();
    }
}
