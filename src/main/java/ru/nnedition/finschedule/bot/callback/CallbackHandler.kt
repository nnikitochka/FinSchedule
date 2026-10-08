package ru.nnedition.finschedule.bot.callback

import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message

abstract class CallbackHandler(@JvmField val key: String) {
    abstract fun handle(data: CallbackData, callbackId: String, from: User, message: Message)
}
