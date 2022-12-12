package com.leontev.physraspbot.services;

import com.leontev.physraspbot.bot.PhysMessages;
import com.leontev.physraspbot.notifications.NotificationsAlreadyEnabledException;
import com.leontev.physraspbot.notifications.PhysNotifications;
import com.leontev.physraspbot.bot.PhysRaspBot;
import com.leontev.physraspbot.notifications.NotificationsNotEnabledException;
import com.leontev.physraspbot.repositories.Group;
import com.leontev.physraspbot.repositories.PhysClass;
import com.leontev.physraspbot.repositories.User;
import com.leontev.physraspbot.repositories.IUserRepository;
import com.leontev.physraspbot.exceptions.NoSuchGroupNumberException;
import com.leontev.physraspbot.exceptions.NoSuchUserException;
import com.leontev.physraspbot.exceptions.NullGroupNumberException;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;


@Service
@AllArgsConstructor
public class UserService {

    private IUserRepository userRepository;
    private RaspService raspService;

    /**
     * Регистрация нового пользователя при первом запуске бота
     * или перерегистрация при обновлении номера групы у старого пользователя
     *
     * @param request запрос от пользователя
     * @return созданного пользователя
     * @throws NoSuchGroupNumberException
     */
    public User createOrUpdateUser(Message request, PhysRaspBot bot) throws NoSuchGroupNumberException {

        String [] splitedRequest = request.getText().split(" +");
        if (splitedRequest.length < 2) {
            throw new NullGroupNumberException();
        }

        String groupNumber = splitedRequest[1];

        boolean notificationsEnabled = false;

        try {
            User currentUser = userRepository.getUser(request.getChat());
            Thread thread = currentUser.getNotificationsThread();
            if (thread != null) {
                thread.interrupt();
                notificationsEnabled = true;
            }
        } catch (NoSuchUserException e) {}

        User user = new User(request.getChat(), Group.getGroupByNumber(groupNumber));

        if (notificationsEnabled) {
            user.setNotificationsThread(new PhysNotifications(user, bot, raspService));
        }
        userRepository.addUser(user);

        return user;
    }

    /**
     *
     * @param request запрос от пользователя
     * @param date дата
     * @return расписание группы пользователя на запрошенную дату
     * или расписание запрошенной группы на запрошенную дату
     *
     * @throws NoSuchUserException
     * @throws NoSuchGroupNumberException
     */
    public String getRasp(Message request, LocalDate date) throws NoSuchUserException, NoSuchGroupNumberException {

        String [] splitedRequest = request.getText().split(" +");

        if (splitedRequest.length < 2) {
            return getUserGroupRasp(request, date);
        }

        Group group = Group.getGroupByNumber(splitedRequest[1]);

        return getOtherGroupRasp(group, date);
    }

    // УВЕДОМЛЕНИЯ
    public void enableNotificationsForUser(Message request, PhysRaspBot bot) throws NoSuchUserException, NotificationsAlreadyEnabledException {
        User user = userRepository.getUser(request.getChat());
        if (user.getNotificationsThread() == null)
            user.setNotificationsThread(new PhysNotifications(user, bot, raspService));
        else
            throw new NotificationsAlreadyEnabledException();
    }

    public void disableNotifications(Message request) throws NoSuchUserException, NotificationsNotEnabledException {
        User user = userRepository.getUser(request.getChat());
        Thread thread = user.getNotificationsThread();
        if (thread == null) {throw new NotificationsNotEnabledException();}
        thread.interrupt();
        user.setNotificationsThread(null);
    }

    // СЛЕДУЮЩАЯ ПАРА
    public String getNextClass(Message request) throws NoSuchUserException, NoSuchGroupNumberException {
        String [] splitedRequest = request.getText().split(" +");

        if (splitedRequest.length < 2) {
            User user = userRepository.getUser(request.getChat());
            return getNextClassForGroup(user.getGroup());
        }

        return getNextClassForGroup(Group.getGroupByNumber(splitedRequest[1]));
    }

    private String getNextClassForGroup(Group group) {
        PhysClass physClass = raspService.getNextClass(group);
        LocalDateTime startDate = physClass.getStartDateAndTime();
        String date = startDate.getDayOfMonth() + " "
                + startDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru")).toUpperCase() + ", "
                + startDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toUpperCase();

        return String.format(PhysMessages.NEXT_CLASS.getMessage(), group, date) + physClass;
    }

    /**
     *
     * @param request запрос от пользователся
     * @param date дата
     * @return расписание группы пользователя на запрошенную дату
     * @throws NoSuchUserException
     */
    private String getUserGroupRasp(Message request, LocalDate date) throws NoSuchUserException {

        try {
            User user = userRepository.getUser(request.getChat());
            return raspService.getRasp(user.getGroup(), date);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param group группа
     * @param date дата
     * @return расписание запрошенной группы на запрошенную дату
     */
    private String getOtherGroupRasp(Group group, LocalDate date) {

        try {
            return raspService.getRasp(group, date);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
