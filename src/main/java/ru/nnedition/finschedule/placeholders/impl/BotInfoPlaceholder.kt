package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.placeholders.Placeholder
import ru.nnedition.finschedule.utils.Parser

class BotInfoPlaceholder : Placeholder("bot_info") {
    override fun process(user: User): String {
        return Parser.placeholders(FinSchedule.getConfig().botInfo, user)
    }
}
