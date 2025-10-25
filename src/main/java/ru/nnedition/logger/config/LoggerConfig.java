package ru.nnedition.logger.config;

import ru.nnedition.logger.Level;

import java.time.format.DateTimeFormatter;

public class LoggerConfig {
    public static String DEFAULT_DATA_FORMAT = "dd.MM.yyyy";
    private String dataFormat = DEFAULT_DATA_FORMAT;
    public String getDataFormat() {return dataFormat;}
    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
        this.dateFormatter = DateTimeFormatter.ofPattern(dataFormat);
    }
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(this.dataFormat);
    public DateTimeFormatter getDateFormatter() {return dateFormatter;}


    public static String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private String timeFormat = DEFAULT_TIME_FORMAT;
    public String getTimeFormat() {return timeFormat;}
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        this.timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
    }

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(this.timeFormat);
    public DateTimeFormatter getTimeFormatter() {return timeFormatter;}

    /**
     * Минимальный уровень логирования
     */
    public static Level DEFAULT_LEVEL = Level.INFO;
    public Level level = DEFAULT_LEVEL;

    /**
     * Формат выводимых в консоль логов.
     * Доступные плейсхолдеры:
     * {date} - текущая дата
     * {time} - текущее время
     * {level} - уровень логирования
     * {name} - имя логгера
     * {trace} - путь к месту вызова логирования (класс:строка)
     * {thread} - имя потока из которого был вызван логгер
     * {message} - сообщение лога
     */
    public static String DEFAULT_FORMAT = "[{date} {time} {level}] ({thread} - {trace}): {message}";
    public String format = DEFAULT_FORMAT;
}
