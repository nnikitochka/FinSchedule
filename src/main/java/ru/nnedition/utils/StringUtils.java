package ru.nnedition.utils;

import org.jetbrains.annotations.NotNull;

public class StringUtils {
    @NotNull
    public static String trimEnd(@NotNull final String str) {
        return str.replaceFirst("\\s+$", "");
    }

    @NotNull
    public static String removeSuffix(@NotNull final String str, @NotNull final String suffix) {
        if (!str.endsWith(suffix)) return str;
        return str.substring(0, str.length() - suffix.length());
    }
}
