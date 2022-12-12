package com.leontev.physraspbot.notifications;

import com.leontev.physraspbot.bot.PhysRaspBot;
import com.leontev.physraspbot.repositories.PhysClass;
import com.leontev.physraspbot.repositories.User;
import com.leontev.physraspbot.services.RaspService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PhysNotifications extends Thread {

    private final User user;
    private final PhysRaspBot bot;
    private final RaspService raspService;

    public PhysNotifications(User user, PhysRaspBot bot, RaspService raspService) {
        this.user = user;
        this.bot = bot;
        this.raspService = raspService;
        start();
    }

    @Override
    public void run() {
        while (true) {
            PhysClass nextClass = raspService.getNextClass(user.getGroup());
            try {
                long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), nextClass.getStartDateAndTime());
                Thread.sleep(seconds * 1000 - 5 * 60 * 1000 + 5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!interrupted()) {
                bot.sendNotification(String.valueOf(user.getChat().getId()), "Пара через 5 минут:\n\n" + nextClass);
                try {
                    Thread.sleep(10*60*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                break;
        }
    }
}
