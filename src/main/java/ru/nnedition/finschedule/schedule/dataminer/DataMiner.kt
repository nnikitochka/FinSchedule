package ru.nnedition.finschedule.schedule.dataminer

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Чисто чтобы как-то обобщить
 */
abstract class DataMiner(
    val name: String
) {
    /**
     * Выполняет GET-запрос и собирает метрики: HTTP-статус, длительность,
     * размер и тип ответа, превью тела при ошибке.
     *
     * Никак не логирует — все статусы отдаются наружу через [DataMineResult],
     * а их запись в историю делает менеджер.
     */
    protected fun sendRequest(
        httpClient: OkHttpClient,
        request: Request,
    ): DataMineResult<String> {
        val url = request.url.toString()
        val startedAt = System.currentTimeMillis()

        return try {
            httpClient.newCall(request).execute().use { response ->
                val text = response.body?.string()
                val meta = FetchMeta(
                    url = url,
                    httpStatus = response.code,
                    httpMessage = response.message,
                    contentType = response.body?.contentType()?.toString(),
                    durationMs = elapsedMs(startedAt),
                    responseBytes = text?.toByteArray()?.size?.toLong() ?: 0L,
                )

                if (text == null) {
                    return DataMineResult.Fail("Запрос вернул пустое (null) тело ответа.", meta = meta)
                }
                if (!response.isSuccessful) {
                    return DataMineResult.Fail(
                        "Сервер вернул ошибку: HTTP ${response.code} ${response.message}.",
                        meta = meta.copy(bodyPreview = text.take(BODY_PREVIEW_LENGTH)),
                    )
                }
                if (text.isBlank()) {
                    return DataMineResult.Fail("Запрос вернул пустую html страницу.", meta = meta)
                }

                DataMineResult.Success(text, meta)
            }
        } catch (e: IOException) {
            DataMineResult.Fail(
                "Ошибка при выполнении запроса к $url: ${e.localizedMessage}",
                e,
                FetchMeta(url = url, durationMs = elapsedMs(startedAt)),
            )
        }
    }

    /** Сколько миллисекунд прошло с момента [startedAt]. */
    protected fun elapsedMs(startedAt: Long): Long = System.currentTimeMillis() - startedAt

    private companion object {
        /** Сколько символов тела ответа сохранять в лог при ошибке. */
        const val BODY_PREVIEW_LENGTH = 300
    }
}
