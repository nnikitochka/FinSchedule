package ru.nnedition.finschedule.utils;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nnedition.finschedule.placeholders.PlaceholderRegistry;

public class Parser {
    private static final PlaceholderRegistry placeholderRegistry = new PlaceholderRegistry();
    static {
        placeholderRegistry.registerDefaults();
    }

    public static String markdownV2(@NotNull final String text) {
        return MarkdownV2Parser.parse(text);
    }

    @NotNull
    public static String placeholders(@NotNull final String text, User user) {
        if (!text.contains("%")) {
            return text;
        }

        final var sb = new StringBuilder(text.length() + 32);
        int lastEnd = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != '%') continue;

            // Ищем закрывающий %
            int closingIndex = text.indexOf('%', i + 1);

            if (closingIndex != -1) {
                final String key = text.substring(i + 1, closingIndex);
                final var placeholder = placeholderRegistry.getPlaceholder(key);

                if (placeholder == null) continue;

                // Добавляем текст до плейсхолдера
                sb.append(text, lastEnd, i);
                // Добавляем обработанный плейсхолдер
                sb.append(placeholder.process(user));

                lastEnd = closingIndex + 1;
                i = closingIndex; // Перепрыгиваем
            }
        }

        // Добавляем оставшийся текст
        sb.append(text, lastEnd, text.length());
        return sb.toString();
    }

    public static String all(@NotNull final User user, @NotNull final String text) {
        return markdownV2(placeholders(text, user));
    }
}
