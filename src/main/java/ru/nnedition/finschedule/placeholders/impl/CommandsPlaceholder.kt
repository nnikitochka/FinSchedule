package ru.nnedition.finschedule.placeholders.impl

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.command.CommandScope
import ru.nnedition.finschedule.placeholders.Placeholder

class CommandsPlaceholder : Placeholder("commands") {
    override fun process(user: User): String {
        val builder = StringBuilder()
        for (command in FinSchedule.getBot().commandRegistry.getCommands()) {
            if (command.scope == CommandScope.ALL_ADMIN_PRIVATE_CHATS && !FinSchedule.getBot().isAdmin(user)) continue

            builder.append(
                FinSchedule.getConfig().commandFormat
                    .replace("{label}", command.label)
                    .replace("{description}", command.description)
            ).append("\n")
        }
        return builder.toString().trimEnd()
    }
}
