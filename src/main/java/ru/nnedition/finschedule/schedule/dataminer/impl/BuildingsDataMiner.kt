package ru.nnedition.finschedule.schedule.dataminer.impl

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import ru.nnedition.finschedule.schedule.buildings.Building
import ru.nnedition.finschedule.schedule.dataminer.DataMineResult
import ru.nnedition.finschedule.schedule.dataminer.DataMiner
import ru.nnedition.finschedule.schedule.dataminer.manager.DataMinerManager
import java.util.regex.Matcher
import java.util.regex.Pattern

class BuildingsDataMiner(
    private val httpClient: OkHttpClient,
    private val manager: DataMinerManager,
) : DataMiner("BuildingsDataMiner") {
    private companion object {
        private const val SCHEDULE_SITE = "http://barnaul.fa.ru/lessons/"
        val request: Request = Request.Builder().url(SCHEDULE_SITE).get().build()

        private val BUILDINGS_REGEX: Pattern = Pattern.compile("^(\\S+)\\s*-\\s*(.+?)\\s*\\((.+?)\\)$")
    }

    fun fetchData(): Map<String, Building>? {
        val startedAt = System.currentTimeMillis()

        val requestResult = sendRequest(httpClient, request)
        if (requestResult is DataMineResult.Fail) {
            manager.reportDataFetchError(
                this,
                requestResult.description,
                requestResult.error,
                requestResult.meta,
                totalDurationMs = elapsedMs(startedAt),
            )
            return null
        }
        if (requestResult !is DataMineResult.Success) {
            manager.reportUnknownResult(this, totalDurationMs = elapsedMs(startedAt))
            return null
        }

        val html = requestResult.value
        val meta = requestResult.meta

        val buildings = extractBuildings(html)
        if (buildings.isEmpty()) {
            manager.reportDataFetchError(
                this,
                "Корпуса не найдены: на странице нет подходящих строк в таблице.",
                meta = meta,
                parsedCount = 0,
                totalDurationMs = elapsedMs(startedAt),
            )
            return null
        }

        manager.reportDataFetchSuccess(
            this,
            "Извлечены корпуса: $buildings",
            meta = meta,
            parsedCount = buildings.size,
            totalDurationMs = elapsedMs(startedAt),
        )
        return buildings
    }

    fun extractBuildings(html: String): Map<String, Building> {
        val buildingMap = mutableMapOf<String, Building>()
        val corpsTable = Jsoup.parse(html).select("table.simple")

        for (element in corpsTable.select("tr td")) {
            val corpsInfo = element.text()
            if (corpsInfo.isBlank()) continue

            val matcher: Matcher = BUILDINGS_REGEX.matcher(corpsInfo)
            if (!matcher.find()) continue

            val shortName = matcher.group(1)

            buildingMap[shortName] = Building(
                shortName,
                matcher.group(2),
                matcher.group(3)
            )
        }

        return buildingMap
    }
}
