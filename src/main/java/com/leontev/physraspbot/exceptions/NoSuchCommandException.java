package com.leontev.physraspbot.exceptions;

import com.leontev.physraspbot.bot.PhysMessages;

public class NoSuchCommandException extends Exception {
    @Override
    public String getMessage() {return PhysMessages.NO_COMMAND.getMessage();}
}
