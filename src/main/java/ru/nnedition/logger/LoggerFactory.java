package ru.nnedition.logger;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public final class LoggerFactory {
    private static final Map<String, Logger> loggers = new HashMap<>();

    public static @NotNull Logger getLogger(String name) {
        var logger = loggers.get(name);
        return logger != null ? logger : Logger.getLogger(name);
    }

    public static void register(Logger logger) {
        loggers.put(logger.getName(), logger);
    }

    public static PrintStream originalOut = System.out;
    public static PrintStream originalErr = System.err;
}
