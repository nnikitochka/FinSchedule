package ru.nnedition.logger.slf4j;

import org.jetbrains.annotations.NotNull;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SLF4JLoggerFactory implements ILoggerFactory {
    private static final Map<String, Logger> loggers = new HashMap<>();

    @Override
    public @NotNull Logger getLogger(@NotNull String name) {
        var logger = loggers.get(name);
        return logger != null ? logger : SLF4JLogger.getLogger(name);
    }

    public static void register(@NotNull SLF4JLogger logger) {
        loggers.put(logger.getName(), logger);
    }
}
