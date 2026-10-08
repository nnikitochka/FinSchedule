package ru.nnedition.finschedule.schedule.dataminer.impl

import okhttp3.OkHttpClient
import okhttp3.Request
import ru.nnedition.finschedule.schedule.dataminer.DataMineResult
import ru.nnedition.finschedule.schedule.dataminer.DataMiner
import ru.nnedition.finschedule.schedule.dataminer.DataMinerManager
import ru.nnedition.finschedule.schedule.groups.Group
import ru.nnedition.finschedule.schedule.groups.GroupsData
import ru.nnedition.logger.Logger
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class GroupsDataMiner(
    private val httpClient: OkHttpClient,
    private val manager: DataMinerManager,
) : DataMiner("GroupsDataMiner") {
    private companion object {
        const val SCHEDULE_SITE = "http://barnaul.fa.ru/lessons/"
        val request = Request.Builder().url(SCHEDULE_SITE).get().build()

        val SELECT_PATTERN: Pattern = Pattern.compile(
            "<select\\s+name=\"groupname\"[^>]*>(.*?)</select>",
            Pattern.DOTALL
        )

        val OPTION_PATTERN: Pattern = Pattern.compile(
            "<option\\s+value=([^>]+)>[^<]+</option>"
        )
    }

    fun fetchData(): List<Group>? {
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

        val selectMatcher = SELECT_PATTERN.matcher(html)
        if (!selectMatcher.find()) return null

        val groups = extractGroups(selectMatcher)

        manager.reportDataFetchSuccess(this, "Извлечены группы: $groups")
        return groups
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
        return DataMineResult.Fail("Ошибка при получении данных групп: ${e.localizedMessage}", e)
    }

    private fun extractGroups(selectMatcher: Matcher): List<Group> {
        val groups: MutableList<Group> = ArrayList()
        val selectContent = selectMatcher.group(1)
        val optionMatcher = OPTION_PATTERN.matcher(selectContent)

        while (optionMatcher.find()) {
            var value = optionMatcher.group(1)
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length - 1)
            }

            if (value.contains("?")) continue

            groups.add(Group(value))
        }

        return groups
    }
}