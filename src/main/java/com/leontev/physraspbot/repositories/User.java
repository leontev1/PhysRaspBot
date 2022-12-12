package com.leontev.physraspbot.repositories;

import com.leontev.physraspbot.notifications.PhysNotifications;
import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Data
public class User {
    @NonNull
    private final Chat chat;
    @NonNull
    private final Group group;

    private PhysNotifications notificationsThread;
}
