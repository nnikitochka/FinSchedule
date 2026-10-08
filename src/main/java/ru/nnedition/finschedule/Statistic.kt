package ru.nnedition.finschedule

class Statistic {
    //@TODO заменить затычку реальной метрикой
    var users: Int = 1

    private val startTime = System.currentTimeMillis()
    val uptime: Long get() = System.currentTimeMillis() - startTime
}