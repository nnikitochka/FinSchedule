package ru.nnedition.logger;

public record Level(
        String name,
        int intValue,
        String color
) {
    public static final String RESET_COLOR = "\u001B[0m";
    private static final String RED_COLOR = "\u001B[31m";
    private static final String GREEN_COLOR = "\u001B[32m";
    private static final String YELLOW_COLOR = "\u001B[33m";
    private static final String BLUE_COLOR = "\u001B[34m";
    private static final String MAGENTA_COLOR = "\u001B[35m";
    private static final String CYAN_COLOR = "\u001B[36m";

    public static final Level ERROR = new Level("ERROR", 1, RED_COLOR);
    public static final Level WARN = new Level("WARN", 2, YELLOW_COLOR);
    public static final Level INFO = new Level("INFO", 3, RESET_COLOR);
    public static final Level SUCCESS = new Level("SUCCESS", 3, GREEN_COLOR);
    public static final Level DEBUG = new Level("DEBUG", 4, RESET_COLOR);
    public static final Level TRACE = new Level("TRACE", 5, RESET_COLOR);
}
