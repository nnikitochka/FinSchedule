package ru.nnedition.finschedule.bot.user.settings

class UserSettings(
    var mailingSettings: MailingSettings
) {
    companion object {
        fun create(): UserSettings = UserSettings(MailingSettings())
    }
}
