package com.leontev.physraspbot.exceptions;

import com.leontev.physraspbot.bot.PhysMessages;

public class NullGroupNumberException extends NoSuchGroupNumberException {
    @Override
    public String getMessage() {
        return PhysMessages.NULL_GROUP.getMessage();
    }
}
