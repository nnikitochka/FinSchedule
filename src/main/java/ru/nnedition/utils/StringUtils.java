package ru.nnedition.utils;

import org.jetbrains.annotations.NotNull;

public class StringUtils {
    @NotNull
    public static String trimEnd(@NotNull String str) {
        return str.replaceFirst("\\s+$", "");
    }
}
