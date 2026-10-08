package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.placeholders.Placeholder
import ru.nnedition.utils.StringUtils

class BuildingsPlaceholder : Placeholder("buildings") {
    override fun process(user: User): String {
        val builder = StringBuilder()
//        for (building in FinSchedule.getSchedule().buildings) {
//            builder.append(building.format()).append('\n')
//        }
        return builder.toString().trimEnd()
    }
}
