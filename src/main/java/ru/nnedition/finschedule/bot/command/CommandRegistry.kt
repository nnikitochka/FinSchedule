package ru.nnedition.finschedule.bot.command

import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat
import ru.nnedition.finschedule.FinBotTG
import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.command.impl.AdminCommand
import ru.nnedition.finschedule.bot.command.impl.HelpCommand
import ru.nnedition.finschedule.bot.command.impl.StartCommand
import ru.nnedition.logger.Logger

class CommandRegistry {
    private val commands: MutableMap<String, Command> = HashMap()
    fun getCommands(): MutableCollection<Command> {
        return this.commands.values
    }

    fun getCommand(name: String): Command? {
        return this.commands[name]
    }

    fun register(command: Command) {
        if (this.commands.containsKey(command.label)) {
            logger.error("Ошибка при регистрации команды: команда с именем " + command.label + " уже зарегистрирована!")
            return
        }

        this.commands[command.label] = command
    }

    fun register(vararg commands: Command) {
        for (command in commands) {
            this.register(command)
        }
    }

    /**
     * Порядок регистрации команд имеет прямое влияние на порядок команд
     * в меню команд телеграма и других подобных приколов.
     */
    fun registerDefaults(bot: FinBotTG) {
        this.register(
            AdminCommand(bot),
            StartCommand(),
            HelpCommand()
        )
    }

    fun groupByBotScopes(): MutableMap<BotCommandScope, MutableList<Command>> {
        val grouped: MutableMap<BotCommandScope, MutableList<Command>> = HashMap()
        val admins = FinSchedule.getBot().admins

        for (command in this.commands.values) {
            val scope = command.scope

            if (scope == CommandScope.ALL_PRIVATE_CHATS) {
                // Добавляем команду для всех приватных чатов
                grouped.computeIfAbsent(
                    BotCommandScopeAllPrivateChats()
                ) { _ -> ArrayList() }
                    .add(command)

                // Также добавляем команду для каждого админского чата
                for (adminId in admins) {
                    grouped.computeIfAbsent(
                        BotCommandScopeChat(adminId)
                    ) { _ -> ArrayList() }
                        .add(command)
                }
            } else if (scope == CommandScope.ALL_ADMIN_PRIVATE_CHATS) {
                // Добавляем команду только для каждого админского чата
                for (adminId in admins) {
                    grouped.computeIfAbsent(
                        BotCommandScopeChat(adminId)
                    ) { _ -> ArrayList() }
                        .add(command)
                }
            }
        }

        return grouped
    }

    companion object {
        private val logger = Logger.getLogger(CommandRegistry::class.java)
    }
}
