package com.leontev.physraspbot.bot;

import com.leontev.physraspbot.notifications.NotificationsAlreadyEnabledException;
import com.leontev.physraspbot.notifications.NotificationsNotEnabledException;
import com.leontev.physraspbot.repositories.User;
import com.leontev.physraspbot.exceptions.NoSuchGroupNumberException;
import com.leontev.physraspbot.exceptions.NoSuchCommandException;
import com.leontev.physraspbot.exceptions.NoSuchUserException;
import com.leontev.physraspbot.services.UserService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PhysRaspBot extends TelegramLongPollingBot {
    @Getter
    private final String botToken = "5334701947:AAFKWsgPiXWc0TAs4Gw9SZJjR1DgrurhoQ0";
    @Getter
    private final String botUsername = "phys_rasp_bot";
    private static final SendMessage response = new SendMessage();
    @NonNull
    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {return;}

        Message request = update.getMessage();
        response.setChatId(request.getChatId().toString());

        try {
            Command command = Command.getCommand(request.getText().split(" +")[0]);

            switch (command) {
                case START: sendRegularMessage(String.format(PhysMessages.START_MESSAGE.getMessage(), request.getChat().getFirstName())); break;
                case GROUP: createOrUpdateUser(request); break;
                case HELP: sendRegularMessage(PhysMessages.HELP_MESSAGE.getMessage()); break;
                case TODAY: sendRasp(request, LocalDate.now()); break;
                case TOMORROW: sendRasp(request, LocalDate.now().plusDays(1)); break;
                case NOTIFY: enableNotifications(request); break;
                case NOTNOTIFY: disableNotifications(request); break;
                case NEXT: whatIsNextClass(request); break;
            }
        } catch (NoSuchCommandException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    private void whatIsNextClass(Message request) {
        try {
            sendRegularMessage(userService.getNextClass(request));
        } catch (NoSuchUserException | NoSuchGroupNumberException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    private void disableNotifications(Message request) {
        try {
            userService.disableNotifications(request);
            sendRegularMessage(PhysMessages.NOTIFICATIONS_DISABLED.getMessage());
        } catch (NoSuchUserException | NotificationsNotEnabledException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    private void enableNotifications(Message request) {
        try {
            userService.enableNotificationsForUser(request, this);
            sendRegularMessage(PhysMessages.NOTIFICATIONS_ENABLED.getMessage());
        } catch (NoSuchUserException | NotificationsAlreadyEnabledException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    private void sendRegularMessage(String message) {
        response.setText(message);

        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendErrorMessage(String message) {
        sendRegularMessage(message);
    }

    public void sendNotification(String chatId, String message) {
        SendMessage notification = new SendMessage();
        notification.setChatId(chatId);
        notification.setText(message);

        try {
            execute(notification);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendRasp(Message request, LocalDate date) {
        try {
            sendRegularMessage(userService.getRasp(request, date));
        } catch (NoSuchUserException | NoSuchGroupNumberException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    private void createOrUpdateUser(Message request) {
        try {
            User user = userService.createOrUpdateUser(request, this);
            sendRegularMessage(String.format(PhysMessages.NEW_USER.getMessage(), user.getGroup()));
        } catch (NoSuchGroupNumberException e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
