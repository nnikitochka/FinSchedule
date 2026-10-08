package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.placeholders.Placeholder

class BuildingsPlaceholder : Placeholder("buildings") {
    override fun process(user: User): String = buildString {
        for (building in FinSchedule.getSchedule().buildings) {
            append(building.format()).append('\n')
        }
    }.trimEnd()
}
