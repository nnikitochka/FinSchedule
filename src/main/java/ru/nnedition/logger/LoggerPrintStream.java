package ru.nnedition.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.OutputStream;
import java.io.PrintStream;

public class LoggerPrintStream extends PrintStream {
    private final Level level;
    private final Logger logger;
    public LoggerPrintStream(@NotNull final OutputStream originOut, @NotNull final Level level) {
        super(originOut);

        this.level = level;
        this.logger = Logger.getLogger(LoggerPrintStream.class.getName()+"_"+level.name());
    }

    @Override
    public void print(boolean b) {
        logger.log(level, String.valueOf(b), null);
    }

    @Override
    public void print(char c) {
        logger.log(level, String.valueOf(c), null);
    }

    @Override
    public void print(int i) {
        logger.log(level, String.valueOf(i), null);
    }

    @Override
    public void print(long l) {
        logger.log(level, String.valueOf(l), null);
    }

    @Override
    public void print(float f) {
        logger.log(level, String.valueOf(f), null);
    }

    @Override
    public void print(double d) {
        logger.log(level, String.valueOf(d), null);
    }

    @Override
    public void print(@NotNull char[] s) {
        logger.log(level, String.valueOf(s), null);
    }

    @Override
    public void print(@Nullable String s) {
        logger.log(level, String.valueOf(s), null);
    }

    @Override
    public void print(@Nullable Object obj) {
        logger.log(level, String.valueOf(obj), null);
    }

    @Override
    public void println() {
        logger.log(level, "", null);
    }

    @Override
    public void println(boolean x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(char x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(int x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(long x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(float x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(double x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(@NotNull char[] x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(@Nullable String x) {
        logger.log(level, String.valueOf(x), null);
    }

    @Override
    public void println(@Nullable Object x) {
        logger.log(level, String.valueOf(x), null);
    }
}
