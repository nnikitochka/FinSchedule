package ru.nnedition.finschedule.schedule.dataminer.impl

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import ru.nnedition.finschedule.schedule.buildings.Building
import ru.nnedition.finschedule.schedule.dataminer.DataMineResult
import ru.nnedition.finschedule.schedule.dataminer.DataMiner
import ru.nnedition.finschedule.schedule.dataminer.DataMinerManager
import ru.nnedition.finschedule.schedule.groups.Group
import ru.nnedition.logger.Logger
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class BuildingsDataMiner(
    private val httpClient: OkHttpClient,
    private val manager: DataMinerManager,
) : DataMiner("BuildingsDataMiner") {
    companion object {
        private val logger = Logger.getLogger(BuildingsDataMiner::class.java)

        private const val SCHEDULE_SITE = "http://barnaul.fa.ru/lessons/"
        val request: Request = Request.Builder().url(SCHEDULE_SITE).get().build()

        private val BUILDINGS_REGEX: Pattern = Pattern.compile("^(\\S+)\\s*-\\s*(.+?)\\s*\\((.+?)\\)$")
    }

    fun fetchData(): Map<String, Building>? {
        val html: String = when (val requestResult = sendRequest()) {
            is DataMineResult.Fail -> {
                manager.reportDataFetchError(this, requestResult.description)
                return null
            }
            is DataMineResult.Success -> requestResult.value
            else -> {
                manager.reportUnknownResult(this)
                return null
            }
        }

        return extractBuildings(html)
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

    private fun sendRequest(): DataMineResult<String> = try {
        httpClient.newCall(request).execute().use { response ->
            val body = response.body
                ?: return DataMineResult.Fail("Запрос вернул пустое (null) тело ответа.")

            val responseText = body.string()

            if (responseText.isBlank()) {
                return DataMineResult.Fail("Запрос вернул пустую html страницу.")
            }

            DataMineResult.Success(responseText)
        }
    } catch (e: IOException) {
        return DataMineResult.Fail("Ошибка при обновлении данных корпусов: ${e.localizedMessage}", e)
    }
}
