package ru.nnedition.finschedule.bot.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.command.impl.AdminCommand;
import ru.nnedition.finschedule.bot.command.impl.HelpCommand;
import ru.nnedition.finschedule.bot.command.impl.StartCommand;
import ru.nnedition.logger.Logger;

import java.util.*;

public final class CommandRegistry {
    private static final Logger logger = Logger.getLogger(CommandRegistry.class);

    private final Map<String, Command> commands = new HashMap<>();
    @NotNull
    public Collection<Command> getCommands() {
        return this.commands.values();
    }
    @Nullable
    public Command getCommand(@NotNull final String name) {
        return this.commands.get(name);
    }

    public void register(@NotNull final Command command) {
        if (this.commands.containsKey(command.getLabel())) {
            logger.error("Ошибка при регистрации команды: команда с именем " + command.getLabel() + " уже зарегистрирована!");
            return;
        }

        this.commands.put(command.getLabel(), command);
    }
    public void register(@NotNull final Command... commands) {
        for (final var command : commands) {
            this.register(command);
        }
    }

    /**
     * Порядок регистрации команд имеет прямое влияние на порядок команд
     * в меню команд телеграма и других подобных приколов.
     */
    public void registerDefaults() {
        this.register(
                new AdminCommand(),
                new StartCommand(),
                new HelpCommand()
        );
    }

    @NotNull
    public Map<BotCommandScope, List<Command>> groupByBotScopes() {
        final Map<BotCommandScope, List<Command>> grouped = new HashMap<>();
        final var admins = FinSchedule.getBot().getAdmins();

        for (final var command : this.commands.values()) {
            final var scope = command.getScope();

            if (scope == CommandScope.ALL_PRIVATE_CHATS) {
                // Добавляем команду для всех приватных чатов
                grouped.computeIfAbsent(
                        new BotCommandScopeAllPrivateChats(),
                        k -> new ArrayList<>()
                ).add(command);

                // Также добавляем команду для каждого админского чата
                for (final var adminId : admins) {
                    grouped.computeIfAbsent(
                            new BotCommandScopeChat(adminId),
                            k -> new ArrayList<>()
                    ).add(command);
                }
            }
            else if (scope == CommandScope.ALL_ADMIN_PRIVATE_CHATS) {
                // Добавляем команду только для каждого админского чата
                for (final var adminId : admins) {
                    grouped.computeIfAbsent(
                            new BotCommandScopeChat(adminId),
                            k -> new ArrayList<>()
                    ).add(command);
                }
            }
        }

        return grouped;
    }
}
