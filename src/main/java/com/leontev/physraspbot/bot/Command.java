package com.leontev.physraspbot.bot;

import com.leontev.physraspbot.exceptions.NoSuchCommandException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    START("/start", "/start - запуск бота"),
    TODAY("/today", "/today - получить расписание на сегодня\n" +
            "/today {группа} - получить расписание группы на сегодня"),
    TOMORROW("/tomorrow", "/tomorrow - получить расписание на завтра\n" +
            "/tomorrow {группа} - получить расписание группы на завтра"),
    NEXT("/next", "/next - какая сделующая пара"),
    GROUP("/group","/group {группа} - добавить/изменить свою группу"),
    NOTIFY("/notify", "/notify - тебе будет приходить уведомление за 5 минут до следующей пары"),
    NOTNOTIFY("/notnotify", "/notnotify - отключить уведомления"),
    HELP("/help", "/help - помощь");

    public static Command getCommand(String strCommand) throws NoSuchCommandException {

        for (Command c: Command.values()) {
            if (c.keyword.equals(strCommand)) {
                return c;
            }
        }
        throw new NoSuchCommandException();
    }

    private final String keyword;
    private final String description;
}
