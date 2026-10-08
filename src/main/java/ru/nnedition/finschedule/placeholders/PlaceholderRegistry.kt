package ru.nnedition.finschedule.placeholders

import ru.nnedition.finschedule.placeholders.impl.AppVersionPlaceholder
import ru.nnedition.finschedule.placeholders.impl.BotInfoPlaceholder
import ru.nnedition.finschedule.placeholders.impl.BotUsersCountPlaceholder
import ru.nnedition.finschedule.placeholders.impl.BuildingsPlaceholder
import ru.nnedition.finschedule.placeholders.impl.CommandsPlaceholder
import ru.nnedition.finschedule.placeholders.impl.ScheduleUpdateDelayPlaceholder
import ru.nnedition.logger.Logger

class PlaceholderRegistry {
    private val placeholders: MutableMap<String, Placeholder> = HashMap()
    fun getPlaceholders(): Collection<Placeholder> {
        return this.placeholders.values
    }

    fun getPlaceholder(key: String): Placeholder? {
        return this.placeholders[key]
    }

    fun register(placeholder: Placeholder) {
        if (this.placeholders.containsKey(placeholder.key)) {
            logger.error("Ошибка при регистрации команды: команда с именем " + placeholder.key + " уже зарегистрирована!")
            return
        }

        this.placeholders[placeholder.key] = placeholder
    }

    fun register(vararg placeholders: Placeholder) {
        for (command in placeholders) {
            this.register(command)
        }
    }

    fun registerDefaults() {
        this.register(
            AppVersionPlaceholder(),
            BotInfoPlaceholder(),
            BotUsersCountPlaceholder(),
            BuildingsPlaceholder(),
            CommandsPlaceholder(),
            ScheduleUpdateDelayPlaceholder()
        )
    }

    companion object {
        private val logger = Logger.getLogger(PlaceholderRegistry::class.java)
    }
}
