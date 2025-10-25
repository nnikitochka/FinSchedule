package ru.nnedition.finschedule.utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {
    // Формат времени используемый на сайте расписания
    public static final String TIME_FORMAT = "HH:mm";
    // Формат даты используемый на сайте расписания
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    // Ну это я не помню зачем, но точно надо
    public static final String DATE_FORMAT_FULL = "dd.MM.yyyy HH:mm:ss";
    // Строковый айди временной зоны Барнаула
    public static final String TIME_ZONE_ID = "GMT+7";
    // Временная зона Барнаула
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(TIME_ZONE_ID);
    public static final ZoneId ZONE_ID = ZoneId.of(TIME_ZONE_ID);

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT;
    static {
        var format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(TIME_ZONE);
        SIMPLE_DATE_FORMAT = format;
    }

    public static final SimpleDateFormat SIMPLE_TIME_FORMAT;
    static {
        var format = new SimpleDateFormat(TIME_FORMAT);
        format.setTimeZone(TIME_ZONE);
        SIMPLE_TIME_FORMAT = format;
    }

    public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT;
    static {
        var format = new SimpleDateFormat(DATE_FORMAT_FULL);
        format.setTimeZone(TIME_ZONE);
        SIMPLE_DATE_TIME_FORMAT = format;
    }

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);


    /**
     * @param days Количество дней, может быть отрицательным.
     * @return Дата через указанное количество дней в формате {@link #DATE_FORMAT},
     * если параметр days отрицательный - вернётся прошедшая дата
     */
    @NotNull
    public static String adjustDay(final int days) {
        return LocalDate.now(ZONE_ID).plusDays(days).format(DATE_FORMATTER);
    }


    /**
     * @param date Дата в формате {@link #DATE_FORMAT}
     * @return День недели заглавными буквами.
     */
    @NotNull
    public static String getDayOfWeek(@NotNull final String date) {
        try {
            var parsedDate = SIMPLE_DATE_FORMAT.parse(date);

            var dayFormat = new SimpleDateFormat("EEEE", Locale.of("ru"));
            dayFormat.setTimeZone(TIME_ZONE);

            String dayOfWeek = dayFormat.format(parsedDate);

            return dayOfWeek.toUpperCase();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date: " + date, e);
        }
    }

    /**
     * @return Текущая дата в формате {@link #DATE_FORMAT}
     */
    @NotNull
    public static String getCurrentDate() {
        return SIMPLE_DATE_FORMAT.format(new Date());
    }
    @NotNull
    public static String getCurrentDateTime() {
        return SIMPLE_DATE_TIME_FORMAT.format(new Date());
    }
}
