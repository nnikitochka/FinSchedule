package ru.nnedition.finschedule.bot.command

import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chat.Chat

/**
 * @param label Название команды
 * @param description Описание команды
 * @param scope В каких чатах разрешено использовать команды, по совместительству используется для регистрации
 * @param register Нужно ли регистрировать команду
 */
abstract class Command(
    @JvmField
    val label: String,
    @JvmField
    val description: String,
    @JvmField
    val scope: CommandScope = CommandScope.ALL_PRIVATE_CHATS,
    private val register: Boolean = true
) {
    constructor(label: String, description: String, register: Boolean) : this(
        label,
        description,
        CommandScope.ALL_PRIVATE_CHATS,
        register
    )

    fun needRegister(): Boolean {
        return this.register
    }

    abstract fun execute(
        args: Array<String>,
        sender: User,
        chat: Chat,
        messageId: Int
    )
}
