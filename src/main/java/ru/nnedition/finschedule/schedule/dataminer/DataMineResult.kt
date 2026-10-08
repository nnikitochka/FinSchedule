package ru.nnedition.finschedule.schedule.dataminer

interface DataMineResult <T> {
    data class Success<T>(val value: T) : DataMineResult<T>

    data class Fail<T>(
        val description: String,
        val error: Throwable? = null,
    ) : DataMineResult<T>
}