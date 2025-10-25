package ru.nnedition.format;

import org.jetbrains.annotations.NotNull;

/**
 * Класс предоставляет формы единиц для правильного склонения числительных
 * в соответствии с правилами русского языка.
 * <p>
 * Применяется для форматирования количественных значений с корректными окончаниями.
 * Примеры использования:
 * - Время: "1 год", "2 года", "5 лет"
 * - Предметы: "1 яблоко", "2 яблока", "5 яблок"
 * - Люди: "1 человек", "2 человека", "5 человек"
 *
 * @param one форма для чисел, оканчивающихся на 1 (кроме 11).
 *            Например: "год", "яблоко", "человек"
 * @param two форма для чисел, оканчивающихся на 2, 3, 4 (кроме 12, 13, 14).
 *            Например: "года", "яблока", "человека"
 * @param many форма для всех остальных случаев (0, 5-20, 25-30 и т.д.).
 *             Например: "лет", "яблок", "человек"
 * @param brief краткая форма единицы измерения.
 *                    Например: "л.", "шт.", "чел."
 */
public record Plural(
        @NotNull String one,
        @NotNull String two,
        @NotNull String many,
        @NotNull String brief
) {
    /**
     * Определяет правильную форму склонения для заданного числа согласно правилам русского языка.
     * <p>
     * Логика склонения:
     * - Для чисел от 11 до 19: используется третья форма (много)
     * - Для чисел, оканчивающихся на 1: используется первая форма (один)
     * - Для чисел, оканчивающихся на 2, 3, 4: используется вторая форма (несколько)
     * - Для всех остальных: используется третья форма (много)
     *
     * @param number число для которого нужно получить форму склонения
     * @param plural объект {@link Plural} с нужными форматами склонения
     * @return правильная форма склонения в виде строки
     */
    @NotNull
    public static String getPluralForm(final long number, @NotNull final Plural plural) {
        final int n = (int) (number % 100);
        if (n >= 11 && n <= 19) return plural.many();
        return switch (n % 10) {
            case 1 -> plural.one();
            case 2, 3, 4 -> plural.two();
            default -> plural.many();
        };
    }
    public static String format(final long number, @NotNull final Plural plural) {
        return number +" "+getPluralForm(number, plural);
    }

    public String getPluralForm(long number) {
        return getPluralForm(number, this);
    }

    public String format(long number) {
        return format(number, this);
    }
}
