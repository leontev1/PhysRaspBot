package com.leontev.physraspbot.bot;

import com.leontev.physraspbot.repositories.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PhysMessages {

    NOTIFICATIONS_ENABLED("Уведомления включены"),
    NOTIFICATIONS_DISABLED("Уведомления отключены."),
    HELP_MESSAGE (Arrays.stream(Command.values()).map(Command::getDescription).collect(Collectors.joining("\n"))
            + "\n\nПоддержка @leontev7"),
    START_MESSAGE ("Привет, %s\n\n" + Command.GROUP.getDescription() + "\n" + Command.HELP.getDescription()),
    NEW_USER ("Твоя группа: %s\n\n" + Command.TODAY.getDescription() + "\n" + Command.NOTIFY.getDescription()
            + "\n" +  Command.HELP.getDescription()),
    NEXT_CLASS("СЛЕДУЮЩАЯ ПАРА У %s ГРУППЫ\n" + "%s\n"),
    // Exceptions
    ILLEGAL_GROUP("Такой группы нет (или пока нет)\n" + "\nДоступные группы: " +
            Group.getValidGroups().stream().map(Object::toString).collect(Collectors.joining(", "))),
    NULL_GROUP("Добавь номер своей группы\n" + "Например: /group 202"),
    NO_COMMAND("Такой команды нет\n\n" + Command.HELP.getDescription()),
    NOTIFICATIONS_NOT_ENABLED("У тебя не включены уведомления"),
    NOTIFICATIONS_ALREADY_ENABLED("Уведомления уже включены.");


    private final String message;
}
