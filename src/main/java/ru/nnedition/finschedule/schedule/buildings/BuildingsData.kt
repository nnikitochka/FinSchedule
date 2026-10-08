package ru.nnedition.finschedule.schedule.buildings

class BuildingsData {
    private val buildingsByShort: MutableMap<String, Building> = HashMap()

    fun getBuildings(): Collection<Building> {
        return buildingsByShort.values
    }

    fun getByShortName(shortName: String): Building? {
        return buildingsByShort[shortName]
    }

    fun updateData() {

    }
}