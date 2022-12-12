package com.leontev.physraspbot.notifications;

import com.leontev.physraspbot.bot.PhysMessages;

public class NotificationsNotEnabledException extends Exception {
    @Override
    public String getMessage() {return PhysMessages.NOTIFICATIONS_NOT_ENABLED.getMessage();}
}
