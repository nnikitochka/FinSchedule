package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.command.CommandScope
import ru.nnedition.finschedule.placeholders.Placeholder
import ru.nnedition.finschedule.schedule.Schedule

class ScheduleUpdateDelayPlaceholder : Placeholder("schedule_update_delay") {
    override fun process(user: User): String {
        return FinSchedule.getConfig().scheduleUpdateDelaySec.toString()
    }
}