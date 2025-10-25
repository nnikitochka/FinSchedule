package ru.nnedition.finschedule.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер для форматирования текста в MarkdownV2 для Telegram Bot API
 */
public class MarkdownV2Parser {

    // Специальные символы, которые нужно экранировать в MarkdownV2
    private static final String SPECIAL_CHARS = "_*[]()~`>#+-=|{}.!";

    // Паттерны для поиска форматирования (только парные конструкции)
    private static final Pattern BOLD_PATTERN = Pattern.compile("\\*([^*\n]+)\\*");
    private static final Pattern ITALIC_PATTERN = Pattern.compile("_([^_\n]+)_");
    private static final Pattern CODE_PATTERN = Pattern.compile("`([^`]+)`");
    private static final Pattern LINK_PATTERN = Pattern.compile("\\[([^]]+)]\\(([^)]+)\\)");

    /**
     * Основной метод парсинга текста в MarkdownV2
     *
     * @param text исходный текст
     * @return отформатированный текст для MarkdownV2
     */
    public static String parse(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Собираем все форматированные участки
        List<FormattedSegment> segments = new ArrayList<>();

        // Находим код (приоритет выше, чтобы не трогать содержимое)
        findSegments(text, CODE_PATTERN, MarkdownType.CODE, segments);

        // Находим ссылки
        findSegments(text, LINK_PATTERN, MarkdownType.LINK, segments);

        // Находим жирный текст (только парные звёздочки)
        findSegments(text, BOLD_PATTERN, MarkdownType.BOLD, segments);

        // Находим курсив (только парные подчёркивания)
        findSegments(text, ITALIC_PATTERN, MarkdownType.ITALIC, segments);

        // Сортируем сегменты по позиции
        segments.sort((a, b) -> Integer.compare(a.start, b.start));

        // Строим результат
        StringBuilder result = new StringBuilder();
        int currentPos = 0;

        for (FormattedSegment segment : segments) {
            // Добавляем обычный текст до сегмента
            if (currentPos < segment.start) {
                String plainText = text.substring(currentPos, segment.start);
                result.append(escapeMarkdownV2(plainText));
            }

            // Добавляем форматированный сегмент
            result.append(segment.formatted);

            currentPos = segment.end;
        }

        // Добавляем оставшийся текст
        if (currentPos < text.length()) {
            String plainText = text.substring(currentPos);
            result.append(escapeMarkdownV2(plainText));
        }

        return result.toString();
    }

    /**
     * Находит все сегменты определённого типа форматирования
     */
    private static void findSegments(String text, Pattern pattern, MarkdownType type,
                                     List<FormattedSegment> segments) {
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // Проверяем, не пересекается ли с уже найденными сегментами
            boolean overlaps = segments.stream()
                    .anyMatch(s -> (start >= s.start && start < s.end) ||
                            (end > s.start && end <= s.end) ||
                            (start <= s.start && end >= s.end));

            if (!overlaps) {
                String content = matcher.group(1);
                String formatted = formatSegment(content, type, matcher);
                segments.add(new FormattedSegment(start, end, formatted));
            }
        }
    }

    /**
     * Форматирует сегмент текста в соответствии с типом
     */
    private static String formatSegment(String content, MarkdownType type, Matcher matcher) {
        switch (type) {
            case BOLD:
                // Экранируем содержимое и добавляем маркеры жирного текста
                return "*" + escapeMarkdownV2(content) + "*";

            case ITALIC:
                return "_" + escapeMarkdownV2(content) + "_";

            case CODE:
                // В коде не нужно экранировать
                return "`" + content + "`";

            case LINK:
                String linkText = matcher.group(1);
                String linkUrl = matcher.group(2);
                return "[" + escapeMarkdownV2(linkText) + "](" + linkUrl + ")";

            default:
                return escapeMarkdownV2(content);
        }
    }

    /**
     * Экранирует специальные символы для MarkdownV2
     */
    private static String escapeMarkdownV2(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder escaped = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (SPECIAL_CHARS.indexOf(c) != -1) {
                escaped.append('\\');
            }
            escaped.append(c);
        }

        return escaped.toString();
    }

    /**
     * Типы форматирования
     */
    private enum MarkdownType {
        BOLD, ITALIC, CODE, LINK
    }

    /**
     * Класс для хранения информации о форматированном сегменте
     */
    private static class FormattedSegment {
        final int start;
        final int end;
        final String formatted;

        FormattedSegment(int start, int end, String formatted) {
            this.start = start;
            this.end = end;
            this.formatted = formatted;
        }
    }

    /**
     * Вспомогательный метод для парсинга многострочного текста
     */
    public static String parseMultiline(String... lines) {
        StringBuilder combined = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            combined.append(lines[i]);
            if (i < lines.length - 1) {
                combined.append("\n");
            }
        }
        return parse(combined.toString());
    }
}