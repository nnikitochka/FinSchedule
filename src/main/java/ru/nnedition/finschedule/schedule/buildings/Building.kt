package ru.nnedition.finschedule.schedule.buildings

import ru.nnedition.finschedule.FinSchedule

@JvmRecord
data class Building(
    val shortName: String,
    val fullName: String,
    val address: String
) {
    fun format(): String {
        return FinSchedule.getConfig().buildingFormat
            .replace("{short_name}", this.shortName)
            .replace("{full_name}", this.fullName)
            .replace("{address}", this.address)
    }
}
