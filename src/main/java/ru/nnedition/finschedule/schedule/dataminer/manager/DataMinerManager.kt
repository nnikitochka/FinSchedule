package ru.nnedition.finschedule.schedule.dataminer.manager

import ru.nnedition.finschedule.schedule.dataminer.DataMiner
import ru.nnedition.finschedule.schedule.dataminer.FetchMeta

/**
 * Собирает полные логи и все статусы всех fetchData:
 * по каждому майнеру хранится история последних [MAX_LOGS_PER_MINER] записей.
 *
 * Логи пишутся только сюда — наружу (в консоль) ничего не выводится.
 */
class DataMinerManager {
    companion object {
        /** Сколько последних записей хранить по каждому майнеру. */
        const val MAX_LOGS_PER_MINER = 200
    }

    private val logsByMiner: MutableMap<String, MutableList<LogResult>> = LinkedHashMap()

    /** Снимок истории логов по каждому майнеру (новые в конце). */
    val resultsLogs: Map<String, List<LogResult>>
        get() = synchronized(this) {
            logsByMiner.mapValues { it.value.toList() }
        }

    fun reportDataFetchSuccess(
        miner: DataMiner,
        description: String,
        meta: FetchMeta? = null,
        parsedCount: Int? = null,
        totalDurationMs: Long? = null,
    ) = addLog(miner, FetchStatus.SUCCESS, description, meta, totalDurationMs, parsedCount, null)

    fun reportDataFetchError(
        miner: DataMiner,
        description: String,
        error: Throwable? = null,
        meta: FetchMeta? = null,
        parsedCount: Int? = null,
        totalDurationMs: Long? = null,
    ) = addLog(miner, FetchStatus.FAIL, description, meta, totalDurationMs, parsedCount, error)

    fun reportUnknownResult(
        miner: DataMiner,
        description: String = "Неизвестный результат выполнения fetchData.",
        meta: FetchMeta? = null,
        totalDurationMs: Long? = null,
    ) = addLog(miner, FetchStatus.UNKNOWN, description, meta, totalDurationMs, null, null)

    private fun addLog(
        miner: DataMiner,
        status: FetchStatus,
        description: String,
        meta: FetchMeta?,
        totalDurationMs: Long?,
        parsedCount: Int?,
        error: Throwable?,
    ) {
        val entry = LogResult(
            miner = miner.name,
            status = status,
            description = description,
            meta = meta,
            totalDurationMs = totalDurationMs,
            parsedCount = parsedCount,
            error = error,
        )

        synchronized(this) {
            val logs = logsByMiner.getOrPut(miner.name) { ArrayList() }
            logs.add(entry)
            while (logs.size > MAX_LOGS_PER_MINER) logs.removeAt(0)
        }
    }

    /** Все логи одного майнера, новые в конце. */
    fun getLogs(minerName: String): List<LogResult> = synchronized(this) {
        logsByMiner[minerName]?.toList() ?: emptyList()
    }

    /** Последняя запись майнера или null, если он ещё ничего не пытался сделать. */
    fun getLastResult(minerName: String): LogResult? = synchronized(this) {
        logsByMiner[minerName]?.lastOrNull()
    }

    /** Все логи всех майнеров в хронологическом порядке. */
    fun getAllLogs(): List<LogResult> = synchronized(this) {
        logsByMiner.values.flatten().sortedBy { it.timestamp }
    }

    /** Очистить историю: одного майнера или, если [minerName] не указан, всех. */
    fun clearLogs(minerName: String? = null) = synchronized(this) {
        if (minerName == null) logsByMiner.clear() else logsByMiner.remove(minerName)
    }
}
