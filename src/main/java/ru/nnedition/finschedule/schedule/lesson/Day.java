package ru.nnedition.finschedule.schedule.lesson;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @param date Дата дня.
 * @param dayOfWeek День недели заглавными буквами.
 * @param lessons Мапа, где ключ - имя группы
 */
public record Day(
        String date,
        String dayOfWeek,
        Map<String, Lesson> lessons
) {
    public static Day getEmptyDay(@NotNull final String date, @NotNull final String dayOfWeek) {
        return new Day(date, dayOfWeek, Map.of());
    }
}
