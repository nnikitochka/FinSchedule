package ru.nnedition.finschedule.schedule.dataminer.manager

import ru.nnedition.finschedule.schedule.dataminer.FetchMeta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Полная запись лога одной попытки fetchData: статус, описание,
 * метрики HTTP-запроса, результаты разбора и стек-трейс ошибки.
 */
data class LogResult(
    /** Имя майнера, выполнившего fetchData. */
    val miner: String,
    /** Итоговый статус попытки. */
    val status: FetchStatus,
    /** Описание того, что произошло. */
    val description: String,
    /** Время завершения попытки. */
    val timestamp: Long = System.currentTimeMillis(),
    /** Метрики HTTP-запроса: url, статус, длительность, размер ответа. */
    val meta: FetchMeta? = null,
    /** Общее время выполнения fetchData в мс (запрос + разбор). */
    val totalDurationMs: Long? = null,
    /** Сколько объектов удалось извлечь из ответа. */
    val parsedCount: Int? = null,
    /** Ошибка, если попытка упала. */
    val error: Throwable? = null,
) {
    /**
     * Читаемая строка полного лога: метки времени, статус, метрики запроса,
     * результаты разбора и стек-трейс ошибки (если была).
     */
    fun format(): String {
        val sb = StringBuilder()
        sb.append(SimpleDateFormat(TIMESTAMP_PATTERN, Locale.getDefault()).format(Date(timestamp)))
            .append(" [").append(status).append("] ")
            .append(miner).append(": ").append(description)

        if (meta != null) {
            sb.append(" | url=").append(meta.url)
                .append(" | http=").append(meta.httpStatus ?: "-")
            if (meta.httpMessage != null) sb.append(" ").append(meta.httpMessage)
            if (meta.contentType != null) sb.append(" | type=").append(meta.contentType)
            sb.append(" | requestMs=").append(meta.durationMs)
            if (meta.responseBytes > 0) sb.append(" | bytes=").append(meta.responseBytes)
            if (!meta.bodyPreview.isNullOrBlank()) sb.append(" | body=").append(meta.bodyPreview)
        }
        if (totalDurationMs != null) sb.append(" | totalMs=").append(totalDurationMs)
        if (parsedCount != null) sb.append(" | parsed=").append(parsedCount)
        if (error != null) sb.append("\n").append(error.stackTraceToString())

        return sb.toString()
    }

    private companion object {
        const val TIMESTAMP_PATTERN = "dd.MM.yyyy HH:mm:ss.SSS"
    }
}
