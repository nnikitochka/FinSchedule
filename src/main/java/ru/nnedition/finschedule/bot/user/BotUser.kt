package ru.nnedition.finschedule.bot.user

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.bot.user.group.UserGroup
import ru.nnedition.finschedule.bot.user.settings.UserSettings

/**
 * Класс предоставляет мета-данные пользователя,
 * нужные для работы бота
 */
open class BotUser(
    val userId: Long,
    private val loginDate: Long,
    var group: UserGroup?,
    val settings: UserSettings
) {
    @JvmOverloads
    constructor(userId: Long, group: UserGroup? = null) : this(
        userId,
        System.currentTimeMillis(),
        group,
        UserSettings.create()
    )

    fun linkWith(user: User): LinkedBotUser {
        return LinkedBotUser(user, this)
    }
}
