package ru.nnedition.finschedule.bot.command.impl

import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chat.Chat
import ru.nnedition.finschedule.FinBotTG
import ru.nnedition.finschedule.bot.command.Command
import ru.nnedition.finschedule.bot.command.CommandScope
import ru.nnedition.finschedule.bot.menu.context.MenuContext
import ru.nnedition.finschedule.bot.menu.impl.admin.AdminMenu

class AdminCommand(bot: FinBotTG) : Command(
    "admin",
    "панель администратора",
    CommandScope.ALL_ADMIN_PRIVATE_CHATS
) {
    private val adminMenu = bot.menuRegistry.getMenu(AdminMenu::class.java)

    override fun execute(args: Array<String>, sender: User, chat: Chat, messageId: Int) {
        val context = MenuContext(chat, sender)

        this.adminMenu.open(context)
    }
}
