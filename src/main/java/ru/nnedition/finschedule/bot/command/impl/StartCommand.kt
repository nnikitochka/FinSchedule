package ru.nnedition.finschedule.bot.command.impl

import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chat.Chat
import org.telegram.telegrambots.meta.api.objects.message.Message
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.command.Command
import ru.nnedition.finschedule.bot.menu.context.MenuContext
import ru.nnedition.finschedule.bot.menu.impl.SelectGroupMenu
import ru.nnedition.finschedule.utils.Parser
import ru.nnedition.finschedule.utils.SendingUtils

class StartCommand : Command("start", "начать", true) {
    private val menu = FinSchedule.getBot().menuRegistry.getMenu(SelectGroupMenu::class.java)

    override fun execute(args: Array<String>, sender: User, chat: Chat, messageId: Int) {
        val message: SendMessage = SendMessage.builder()
            .chatId(chat.id)
            .text(Parser.all(sender, FinSchedule.getConfig().newUserStart))
            .parseMode(ParseMode.MARKDOWNV2)
            .build()

        val sent = SendingUtils.tryExecute(message) ?: return

        this.menu.open(MenuContext(chat, sender))
    }
}
