package com.leontev.physraspbot.repositories;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PhysClass {

    private static final String SEPARATOR = "-----------------------------------------\n";

    enum ClassNumber {
        FIRST(LocalTime.of(9, 0), LocalTime.of(10, 35)),
        SECOND(LocalTime.of(10, 50), LocalTime.of(12, 25)),
        THIRD(LocalTime.of(13, 30), LocalTime.of(15, 5)),
        FOURTH(LocalTime.of(15, 20), LocalTime.of(16, 55)),
        FIFTH(LocalTime.of(17, 5), LocalTime.of(18, 40));

        ClassNumber(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.timeString = startTime.toString() + " - " + endTime.toString();
        }
        private final LocalTime startTime;
        private final String timeString;
    }

    private final String description;
    private final ClassNumber classNumber;
    @Getter
    private final LocalDateTime startDateAndTime;

    public PhysClass(ClassNumber classNumber, LocalDate date, String description) {
        this.classNumber = classNumber;
        this.startDateAndTime = date.atTime(classNumber.startTime.getHour(), classNumber.startTime.getMinute());
        this.description = description;
    }

    @Override
    public String toString() {return SEPARATOR + classNumber.timeString  + " " + this.description + "\n";}
}
