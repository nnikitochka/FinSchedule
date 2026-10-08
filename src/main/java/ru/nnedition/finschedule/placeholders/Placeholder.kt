package ru.nnedition.finschedule.placeholders

import org.telegram.telegrambots.meta.api.objects.User

abstract class Placeholder(
    @JvmField
    val key: String
) {
    abstract fun process(user: User): String
}
