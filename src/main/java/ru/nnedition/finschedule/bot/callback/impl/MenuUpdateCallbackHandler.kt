package ru.nnedition.finschedule.bot.callback.impl

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.callback.CallbackData
import ru.nnedition.finschedule.bot.callback.CallbackHandler
import ru.nnedition.finschedule.bot.callback.impl.MenuUpdateCallbackData.Companion.getMenu
import ru.nnedition.finschedule.bot.menu.context.EditMenuContext
import ru.nnedition.finschedule.utils.SendingUtils

class MenuUpdateCallbackHandler : CallbackHandler("mnUpd") {
    override fun handle(data: CallbackData, callbackId: String, from: User, message: Message) {
        val menu = getMenu(data) ?: return

        val context = EditMenuContext(message.chat, from, message.messageId)
        val isSuccess = menu.update(context)

        if (isSuccess) return

        val answer: AnswerCallbackQuery = AnswerCallbackQuery.builder()
            .callbackQueryId(callbackId)
            .text(FinSchedule.getConfig().nothingChanged)
            .build()

        SendingUtils.tryExecute(answer)
    }
}
