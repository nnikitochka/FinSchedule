package ru.nnedition.finschedule.placeholders.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.command.Command;
import ru.nnedition.finschedule.bot.command.CommandScope;
import ru.nnedition.finschedule.placeholders.Placeholder;
import ru.nnedition.utils.StringUtils;

public class CommandsPlaceholder extends Placeholder {
    public CommandsPlaceholder() {
        super("commands");
    }

    @Override
    public String process(User user) {
        final var builder = new StringBuilder();
        for (final var command : FinSchedule.getBot().getCommandRegistry().getCommands()) {
            if (command.getScope() == CommandScope.ALL_ADMIN_PRIVATE_CHATS && !FinSchedule.getBot().isAdmin(user)) continue;

            builder.append(FinSchedule.getConfig().commandFormat
                    .replace("{label}", command.getLabel())
                    .replace("{description}", command.getDescription())
            ).append("\n");
        }
        return StringUtils.trimEnd(builder.toString());
    }
}
