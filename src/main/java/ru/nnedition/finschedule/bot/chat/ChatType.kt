package ru.nnedition.finschedule.bot.chat

import ru.nnedition.format.Plural

enum class ChatType(
    @JvmField
    val translate: Plural
) {
    USER(Plural("личном чате", "личных чата", "личных чатов", "лс")),
    GROUP(Plural("группе", "группы", "групп", "группе")),
    CHANNEL(Plural("канал", "канала", "каналов", "канале")),
    SUPERGROUP(Plural("форуме", "форумах", "форумов", "форуме"));
}
