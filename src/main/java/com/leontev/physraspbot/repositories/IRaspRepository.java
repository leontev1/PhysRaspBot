package com.leontev.physraspbot.repositories;

import java.time.LocalDate;

public interface IRaspRepository {
    String getRasp(Group group, LocalDate date);

    PhysClass getNextClass(Group group);
}
