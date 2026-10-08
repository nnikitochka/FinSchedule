package ru.nnedition.finschedule.bot.chat

import org.telegram.telegrambots.meta.api.objects.chat.Chat

object ChatUtils {
    /**
     * @param chat чат, тип которого нужно получить
     * @return тип чата, может быть null, если появится новый тип чатов в телеграме
     */
    @JvmStatic
    fun getChatType(chat: Chat): ChatType? = when {
        chat.isUserChat -> ChatType.USER
        chat.isGroupChat -> ChatType.GROUP
        chat.isChannelChat -> ChatType.CHANNEL
        chat.isSuperGroupChat -> ChatType.SUPERGROUP
        else -> null
    }
}
