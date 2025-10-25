package ru.nnedition.logger.slf4j;

import org.slf4j.Logger;
import org.slf4j.Marker;
import ru.nnedition.logger.Level;

public class SLF4JLogger implements Logger {
    private final ru.nnedition.logger.Logger logger;
    private SLF4JLogger(ru.nnedition.logger.Logger logger) {
        this.logger = logger;
        SLF4JLoggerFactory.register(this);
    }
    public static SLF4JLogger getLogger(String name) {
        var logger = ru.nnedition.logger.Logger.getLogger(name);
        return new SLF4JLogger(logger);
    }

    public void log(Level level, String message, Throwable throwable, Object... args) {
        message = message == null ? "null" : args.length == 0 ? message : format(message, args);
        this.logger.log(level, message, throwable);
    }

    private String format(String format, Object... args) {
        StringBuilder sb = new StringBuilder();
        int argIndex = 0;
        int i = 0;

        while (i < format.length()) {
            if (i + 1 < format.length() && format.charAt(i) == '{' && format.charAt(i + 1) == '}') {
                Object arg = argIndex < args.length ? args[argIndex] : null;
                sb.append(arg != null ? arg : "null");
                argIndex++;
                i += 2;
            } else {
                sb.append(format.charAt(i));
                i++;
            }
        }

        return sb.toString();
    }

    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isLevelLogEnabled(Level.TRACE);
    }

    @Override
    public void trace(String msg) {
        this.log(Level.TRACE, msg, null);
    }

    @Override
    public void trace(String msg, Object arg) {
        this.log(Level.TRACE, msg, null, arg);
    }

    @Override
    public void trace(String msg, Object arg1, Object arg2) {
        this.log(Level.TRACE, msg, null, arg1, arg2);
    }

    @Override
    public void trace(String msg, Object... arguments) {
        this.log(Level.TRACE, msg, null, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.log(Level.TRACE, msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.logger.isLevelLogEnabled(Level.TRACE);
    }

    @Override
    public void trace(Marker marker, String msg) {
        this.log(Level.TRACE, msg, null);
    }

    @Override
    public void trace(Marker marker, String msg, Object arg) {
        this.log(Level.TRACE, msg, null, arg);
    }

    @Override
    public void trace(Marker marker, String msg, Object arg1, Object arg2) {
        this.log(Level.TRACE, msg, null, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String msg, Object... argArray) {
        this.log(Level.TRACE, msg, null, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        this.log(Level.TRACE, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isLevelLogEnabled(Level.DEBUG);
    }

    @Override
    public void debug(String msg) {
        this.log(Level.DEBUG, msg, null);
    }

    @Override
    public void debug(String msg, Object arg) {
        this.log(Level.DEBUG, msg, null, arg);
    }

    @Override
    public void debug(String msg, Object arg1, Object arg2) {
        this.log(Level.DEBUG, msg, null, arg1, arg2);
    }

    @Override
    public void debug(String msg, Object... arguments) {
        this.log(Level.DEBUG, msg, null, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.log(Level.DEBUG, msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.logger.isLevelLogEnabled(Level.DEBUG);
    }

    @Override
    public void debug(Marker marker, String msg) {
        this.log(Level.DEBUG, msg, null);
    }

    @Override
    public void debug(Marker marker, String msg, Object arg) {
        this.log(Level.DEBUG, msg, null, arg);
    }

    @Override
    public void debug(Marker marker, String msg, Object arg1, Object arg2) {
        this.log(Level.DEBUG, msg, null, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String msg, Object... arguments) {
        this.log(Level.DEBUG, msg, null, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.log(Level.DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isLevelLogEnabled(Level.INFO);
    }

    @Override
    public void info(String msg) {
        this.log(Level.INFO, msg, null);
    }

    @Override
    public void info(String msg, Object arg) {
        this.log(Level.INFO, msg, null, arg);
    }

    @Override
    public void info(String msg, Object arg1, Object arg2) {
        this.log(Level.INFO, msg, null, arg1, arg2);
    }

    @Override
    public void info(String msg, Object... arguments) {
        this.log(Level.INFO, msg, null, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.log(Level.INFO, msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.logger.isLevelLogEnabled(Level.INFO);
    }

    @Override
    public void info(Marker marker, String msg) {
        this.log(Level.INFO, msg, null);
    }

    @Override
    public void info(Marker marker, String msg, Object arg) {
        this.log(Level.INFO, msg, null, arg);
    }

    @Override
    public void info(Marker marker, String msg, Object arg1, Object arg2) {
        this.log(Level.INFO, msg, null, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String msg, Object... arguments) {
        this.log(Level.INFO, msg, null, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.log(Level.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isLevelLogEnabled(Level.WARN);
    }

    @Override
    public void warn(String msg) {
        this.log(Level.WARN, msg, null);
    }

    @Override
    public void warn(String msg, Object arg) {
        this.log(Level.WARN, msg, null, arg);
    }

    @Override
    public void warn(String msg, Object... arguments) {
        this.log(Level.WARN, msg, null, arguments);
    }

    @Override
    public void warn(String msg, Object arg1, Object arg2) {
        this.log(Level.WARN, msg, null, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.log(Level.WARN, msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.logger.isLevelLogEnabled(Level.WARN);
    }

    @Override
    public void warn(Marker marker, String msg) {
        this.log(Level.WARN, msg, null);
    }

    @Override
    public void warn(Marker marker, String msg, Object arg) {
        this.log(Level.WARN, msg, null, arg);
    }

    @Override
    public void warn(Marker marker, String msg, Object arg1, Object arg2) {
        this.log(Level.WARN, msg, null, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String msg, Object... arguments) {
        this.log(Level.WARN, msg, null, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.log(Level.WARN, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isLevelLogEnabled(Level.ERROR);
    }

    @Override
    public void error(String msg) {
        this.log(Level.ERROR, msg, null);
    }

    @Override
    public void error(String msg, Object arg) {
        this.log(Level.ERROR, msg, null, arg);
    }

    @Override
    public void error(String msg, Object arg1, Object arg2) {
        this.log(Level.ERROR, msg, null, arg1, arg2);
    }

    @Override
    public void error(String msg, Object... arguments) {
        this.log(Level.ERROR, msg, null, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.log(Level.ERROR, msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.logger.isLevelLogEnabled(Level.ERROR);
    }

    @Override
    public void error(Marker marker, String msg) {
        this.log(Level.ERROR, msg, null);
    }

    @Override
    public void error(Marker marker, String msg, Object arg) {
        this.log(Level.ERROR, msg, null, arg);
    }

    @Override
    public void error(Marker marker, String msg, Object arg1, Object arg2) {
        this.log(Level.ERROR, msg, null, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String msg, Object... arguments) {
        this.log(Level.ERROR, msg, null, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.log(Level.ERROR, msg, t);
    }
}
