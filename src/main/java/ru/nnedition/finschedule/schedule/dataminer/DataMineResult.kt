package ru.nnedition.finschedule.schedule.dataminer

interface DataMineResult <T> {
    data class Success<T>(
        val value: T,
        val meta: FetchMeta,
    ) : DataMineResult<T>

    data class Fail<T>(
        val description: String,
        val error: Throwable? = null,
        val meta: FetchMeta? = null,
    ) : DataMineResult<T>
}
