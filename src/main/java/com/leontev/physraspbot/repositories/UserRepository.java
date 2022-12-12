package com.leontev.physraspbot.repositories;

import com.leontev.physraspbot.exceptions.NoSuchUserException;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.HashMap;
import java.util.Map;


@Repository
public class UserRepository implements IUserRepository {
    private final Map<Chat, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {users.put(user.getChat(), user);}

    @Override
    public User getUser(final Chat chat) throws NoSuchUserException {

        User user = users.get(chat);
        if (user == null) {
            throw new NoSuchUserException();
        }
        return user;
    }
}
