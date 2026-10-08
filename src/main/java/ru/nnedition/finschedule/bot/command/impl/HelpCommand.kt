package ru.nnedition.finschedule.bot.command.impl

import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chat.Chat
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.command.Command
import ru.nnedition.finschedule.bot.command.CommandScope
import ru.nnedition.finschedule.utils.Parser
import ru.nnedition.finschedule.utils.SendingUtils

class HelpCommand : Command("help", "помощь", CommandScope.ALL_PRIVATE_CHATS, true) {
    override fun execute(args: Array<String>, sender: User, chat: Chat, messageId: Int) {
        val message: SendMessage = SendMessage.builder()
            .chatId(chat.id)
            .text(Parser.all(sender, FinSchedule.getConfig().help))
            .parseMode(ParseMode.MARKDOWNV2)
            .build()

        SendingUtils.tryExecute(message)
    }
}
