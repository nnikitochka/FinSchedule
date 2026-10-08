package ru.nnedition.finschedule.schedule.dataminer.impl

import okhttp3.OkHttpClient
import okhttp3.Request
import ru.nnedition.finschedule.schedule.dataminer.DataMineResult
import ru.nnedition.finschedule.schedule.dataminer.DataMiner
import ru.nnedition.finschedule.schedule.dataminer.manager.DataMinerManager
import ru.nnedition.finschedule.schedule.groups.Group
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

        val selectMatcher = SELECT_PATTERN.matcher(html)
        if (!selectMatcher.find()) {
            manager.reportDataFetchError(
                this,
                "На странице не найден блок <select name=\"groupname\"> со списком групп.",
                meta = meta,
                totalDurationMs = elapsedMs(startedAt),
            )
            return null
        }

        val groups = extractGroups(selectMatcher)
        if (groups.isEmpty()) {
            manager.reportDataFetchError(
                this,
                "Блок со списком групп найден, но ни одной группы извлечь не удалось.",
                meta = meta,
                parsedCount = 0,
                totalDurationMs = elapsedMs(startedAt),
            )
            return null
        }

        manager.reportDataFetchSuccess(
            this,
            "Извлечены группы: $groups",
            meta = meta,
            parsedCount = groups.size,
            totalDurationMs = elapsedMs(startedAt),
        )
        return groups
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
