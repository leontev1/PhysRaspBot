package com.leontev.physraspbot.exceptions;

import com.leontev.physraspbot.bot.PhysMessages;

public class NoSuchGroupNumberException extends Exception {
    @Override
    public String getMessage() {
        return PhysMessages.ILLEGAL_GROUP.getMessage();
    }
}
