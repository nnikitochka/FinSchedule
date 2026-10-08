package ru.nnedition.finschedule.schedule.dataminer

/**
 * Метрики одного HTTP-запроса: всё, что удалось собрать при его выполнении.
 */
data class FetchMeta(
    /** Полный URL запроса. */
    val url: String,
    /** HTTP-код ответа, null — ответа не было (сеть/таймаут/ошибка соединения). */
    val httpStatus: Int? = null,
    /** Текст статуса ("OK", "Internal Server Error"...). */
    val httpMessage: String? = null,
    /** Content-Type тела ответа. */
    val contentType: String? = null,
    /** Длительность запроса в мс (включая чтение тела). */
    val durationMs: Long = 0,
    /** Размер тела ответа в байтах. */
    val responseBytes: Long = 0,
    /** Превью тела ответа — сохраняется только при ошибке. */
    val bodyPreview: String? = null,
)