package com.leontev.physraspbot.repositories;

import com.leontev.physraspbot.exceptions.NoSuchGroupNumberException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Group {

    private final int groupNumber;
    @Getter
    private static final Set<Integer> validGroups = Set.of(202, 209, 210);

    public static Group getGroupByNumber(String strNumber) throws NoSuchGroupNumberException {
        try {
            final int groupNumber = Integer.parseInt(strNumber);
            if (!validGroups.contains(groupNumber)) {
                throw new NoSuchGroupNumberException();
            }
            return new Group(groupNumber);
        } catch (NumberFormatException e) {
            throw new NoSuchGroupNumberException();
        }
    }

    @Override
    public String toString() {return String.valueOf(groupNumber);}
}
