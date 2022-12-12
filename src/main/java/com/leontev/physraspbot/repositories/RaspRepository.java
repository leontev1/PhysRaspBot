package com.leontev.physraspbot.repositories;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RaspRepository implements IRaspRepository {

    private static final String NO_CLASSES_STRING = "\nНет пар. Отдыхаем.";

    private static final Map<DayOfWeek, String> DAY_OF_WEEK_STRING_MAP = Map.of(
            DayOfWeek.MONDAY, "monday",
            DayOfWeek.TUESDAY, "tuesday",
            DayOfWeek.WEDNESDAY, "wednesday",
            DayOfWeek.THURSDAY, "thursday",
            DayOfWeek.FRIDAY, "friday",
            DayOfWeek.SATURDAY, "saturday"
    );

    @Override
    public String getRasp(Group group, LocalDate date)  {

        JSONObject jo = getJSONObjectForThisDayAndGroup(group, date);

        if (jo == null) return NO_CLASSES_STRING;

        List<PhysClass> physClasses = getClassesForDay(jo, date);

        if (physClasses.isEmpty()) return NO_CLASSES_STRING;

        return physClasses.stream().map(PhysClass::toString).collect(Collectors.joining());
    }

    @Override
    public PhysClass getNextClass(Group group) {
        LocalDateTime currentDateAndTime = LocalDate.now().atTime(LocalTime.now());

        while (true) {

            List<PhysClass> physClasses = getClassesForDay(getJSONObjectForThisDayAndGroup(group, currentDateAndTime.toLocalDate()), currentDateAndTime.toLocalDate());

            if (!physClasses.isEmpty()) {

                for (PhysClass c : physClasses) {
                    if (c.getStartDateAndTime().isAfter(currentDateAndTime))
                        return c;
                }
            }

            if (currentDateAndTime.getDayOfWeek().equals(DayOfWeek.SATURDAY))
                currentDateAndTime = currentDateAndTime.plusDays(2).toLocalDate().atTime(0,0,0);
            else
                currentDateAndTime = currentDateAndTime.plusDays(1).toLocalDate().atTime(0,0,0);
        }
    }

    private List<PhysClass> getClassesForDay(JSONObject jo, LocalDate date) {

        List<PhysClass> physClasses = new ArrayList<>();

        int i = 1;
        for (PhysClass.ClassNumber classNumber: PhysClass.ClassNumber.values()) {
            Object description = jo.get(String.valueOf(i));
            if (description != null) {
                physClasses.add(new PhysClass(classNumber, date, description.toString()));
            }
            i ++;
        }

        return physClasses;
    }

    private JSONObject getJSONObjectForThisDayAndGroup(Group group, LocalDate date) {
        String objName = DAY_OF_WEEK_STRING_MAP.get(date.getDayOfWeek());
        if (objName == null) return null;

        Object obj;
        try {
            obj = new JSONParser().parse(new FileReader("src/main/resources/" + group.toString() + ".json"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject jo = (JSONObject) obj;

        return (JSONObject) jo.get(objName);
    }
}
