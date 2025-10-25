package ru.nnedition.finschedule.bot.monitor;

import java.io.File;
import java.text.DecimalFormat;

public final class ResourceMonitor {
    private ResourceMonitor() {}

    public static File dbFile = new File("data.db");

    // Константы для быстрого преобразования
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final double BYTES_TO_MB = 1.0 / (1024.0 * 1024.0);  // ~0.00000095367432


    /**
     * @return текущее потребление памяти в МБ.
     */
    public static double getMemoryUsage() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return usedMemory * BYTES_TO_MB;
    }
    public static String getMemoryUsageFormated() {
        return format.format(getMemoryUsage());
    }

    /**
     * @return выделенный объём памяти в МБ
     */
    public static double getMaxMemory() {
        return Runtime.getRuntime().maxMemory() * BYTES_TO_MB;
    }
    public static String getMaxMemoryFormated() {
        return format.format(getMaxMemory());
    }

    /**
     * Получает общее выделенное приложению объём памяти в МБ.
     */
    public static double getAllocatedMemory() {
        return Runtime.getRuntime().totalMemory() * BYTES_TO_MB;
    }
    public static String getAllocatedMemoryFormated() {
        return format.format(getAllocatedMemory());
    }

    /**
     * Получает количество активных потоков
     */
    public static int getActiveThreadCount() {
        return Thread.activeCount();
    }
    /**
     * Получает размер файла в МБ
     */
    public static double getFileSize(File file) {
        if (!file.exists())
            return 0;
        return file.length() * BYTES_TO_MB;
    }
    public static String getFileSizeFormated(File file) {
        return format.format(getFileSize(file));
    }

    public static String getText() {
        return """
                === Информация о ресурсах ===" +
                "Текущее потребление памяти: {memory_usage} МБ
                "Выделенный объём памяти: {allocated_memory} МБ
                "Максимальный объём памяти: {max_memory} МБ
                "Активных потоков: {active_threads}
                "Размер БД ({db_file}): {db_size} МБ
                =============================
                """.replace("{memory_usage}", getMemoryUsageFormated())
                .replace("{allocated_memory}", getAllocatedMemoryFormated())
                .replace("{max_memory}", getMaxMemoryFormated())
                .replace("{active_threads}", String.valueOf(getActiveThreadCount()))
                .replace("{db_file}", dbFile.getName())
                .replace("{db_size}", getFileSizeFormated(dbFile));
    }
}
