package ru.nnedition.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.logger.config.LoggerConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Supplier;

public final class Logger {
    private final String name;
    public String getName() { return this.name; }
    private Logger(String name) {
        this.name = name;
        LoggerFactory.register(this);
    }

    private LoggerConfig config = new LoggerConfig();
    public LoggerConfig getConfig() { return this.config; }
    public void withConfig(@NotNull LoggerConfig config) { this.config = config; }
    public boolean isLevelLogEnabled(Level level) { return this.config.level.intValue() >= level.intValue(); }

    public static @NotNull Logger getLogger(@NotNull Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }
    public static @NotNull Logger getLogger(@NotNull String name) {
        return new Logger(name);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public void log(
            @NotNull Level level,
            @Nullable String message,
            @Nullable Throwable throwable
    ) {
        if (!isLevelLogEnabled(level)) return;

        message = message == null ? "null" : message;

        var logMsg = new StringBuilder()
                .append(level.color())
                .append(formatLog(level, message))
                .append(Level.RESET_COLOR).toString();
        
        LoggerFactory.originalOut.println(logMsg);

        if (throwable != null) {
            throwable.printStackTrace();
//            for (final var trace : throwable.getStackTrace()) {
//                LoggerFactory.originalErr.println(trace);
//            }
        }
    }

    public void error(@Nullable String message) {
        log(Level.ERROR, message, null);
    }
    public void error(@Nullable String message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    public void warn(@Nullable String message) {
        log(Level.WARN, message, null);
    }
    public void warn(@Nullable String message, Throwable t) {
        log(Level.WARN, message, t);
    }

    public void info(@Nullable String message) {
        log(Level.INFO, message, null);
    }
    public void info(@Nullable String message, Throwable t) {
        log(Level.INFO, message, t);
    }

    public void success(@Nullable String message) {
        log(Level.SUCCESS, message, null);
    }

    public void debug(@Nullable String message) {
        log(Level.DEBUG, message, null);
    }
    public void debug(@Nullable String message, Throwable t) {
        log(Level.DEBUG, message, t);
    }

    public void trace(@Nullable String message) {
        log(Level.TRACE, message, null);
    }
    public void trace(@Nullable String message, Throwable t) {
        log(Level.TRACE, message, t);
    }


    public String formatLog(Level level, String text) {
        String result = this.config.format;
        result = replace(result, "{date}", () -> LocalDate.now().format(this.config.getDateFormatter()));
        result = replace(result, "{time}", () -> LocalTime.now().format(this.config.getTimeFormatter()));
        result = replace(result, "{level}", level::name);
        result = replace(result, "{trace}", () -> {
            StackTraceElement[] traces = new Throwable().getStackTrace();
            //LoggerFactory.originalOut.println(Arrays.stream(traces).map(Object::toString).collect(Collectors.toList()));
            String loggerPackage = Logger.class.getPackage().getName();
            for (StackTraceElement trace : traces) {
                String className = trace.getClassName();
                if (className.startsWith(loggerPackage)) continue;
                return trace.getClassName()+"."+trace.getMethodName()+":"+trace.getLineNumber();
            }
            return traces.length > 0 ? traces[traces.length - 1].toString() : "";
        });
        result = replace(result, "{thread}", () -> Thread.currentThread().getName());
        return result.replace("{message}", text)
                .replace("{name}", this.name);
    }

    public static String replace(String input, String placeholder, Supplier<String> replacement) {
        return input.contains(placeholder) ? input.replace(placeholder, replacement.get()) : input;
    }
}
