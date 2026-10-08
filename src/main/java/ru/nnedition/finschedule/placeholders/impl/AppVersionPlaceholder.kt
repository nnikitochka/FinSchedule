package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.ProjectInfo
import ru.nnedition.finschedule.placeholders.Placeholder

class AppVersionPlaceholder : Placeholder("app_version") {
    override fun process(user: User): String {
        return ProjectInfo.VERSION
    }
}
