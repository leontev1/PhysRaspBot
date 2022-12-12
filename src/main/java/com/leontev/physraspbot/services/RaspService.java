package com.leontev.physraspbot.services;

import com.leontev.physraspbot.bot.PhysMessages;
import com.leontev.physraspbot.repositories.PhysClass;
import com.leontev.physraspbot.repositories.Group;
import com.leontev.physraspbot.repositories.IRaspRepository;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
@AllArgsConstructor
public class RaspService {

    private IRaspRepository raspRepository;

    /**
     *
     * @param group группа
     * @param date дата
     * @return расписание группы на запрошенную дату
     * @throws IOException
     * @throws ParseException
     */
    public String getRasp(Group group, LocalDate date) throws IOException, ParseException {

        final String title = "РАСПИСАНИЕ - "
                + date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toUpperCase()
                + " - " + group.toString() + " ГРУППА\n";

        return  title + raspRepository.getRasp(group, date);
    }

    public PhysClass getNextClass(Group group) {return raspRepository.getNextClass(group);}
}
