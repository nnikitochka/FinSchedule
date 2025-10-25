package ru.nnedition.finschedule.schedule.lesson;

import ru.nnedition.finschedule.schedule.buildings.Building;
import ru.nnedition.finschedule.schedule.groups.Group;

import java.util.List;

public record Lesson(
        int inTurn,
        String startTime,
        String endTime,
        String discipline,
        String type,
        String audience,
        Building building,
        String teacher,
        List<Group> combined
) {
}
