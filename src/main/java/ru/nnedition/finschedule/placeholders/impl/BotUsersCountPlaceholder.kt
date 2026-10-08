package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.placeholders.Placeholder

class BotUsersCountPlaceholder : Placeholder("users_count") {
    override fun process(user: User): String = FinSchedule.statistics.users.toString()
}
