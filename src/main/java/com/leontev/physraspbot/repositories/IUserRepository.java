package com.leontev.physraspbot.repositories;

import com.leontev.physraspbot.exceptions.NoSuchUserException;
import org.telegram.telegrambots.meta.api.objects.Chat;

public interface IUserRepository {
    void addUser(User user);
    User getUser(Chat chat) throws NoSuchUserException;
}
