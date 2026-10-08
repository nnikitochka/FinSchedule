package ru.nnedition.finschedule.bot.user

import org.telegram.telegrambots.meta.api.objects.User
import ru.nnedition.finschedule.bot.user.group.UserGroup

/**
 * Класс предназначен для передачи его в качестве аргумента
 * и не подразумевает долгосрочного хранения в памяти.
 */
class LinkedBotUser(
    val linkedUser: User,
    group: UserGroup?
) : BotUser(linkedUser.id, group) {
    constructor(linkedUser: User, botUser: BotUser) : this(linkedUser, botUser.group)
}
