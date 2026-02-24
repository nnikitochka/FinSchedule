package ru.nnedition.finschedule.bot.command.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.command.Command;
import ru.nnedition.finschedule.bot.menu.context.MenuContext;
import ru.nnedition.finschedule.bot.menu.impl.SelectGroupMenu;
import ru.nnedition.finschedule.utils.Parser;
import ru.nnedition.finschedule.utils.SendingUtils;

import java.util.List;

public class StartCommand extends Command {
    public StartCommand() {
        super("start", "начать", true);
    }

    private final SelectGroupMenu menu = FinSchedule.getBot().getMenuRegistry().getMenu(SelectGroupMenu.class);

    @Override
    public void execute(@NotNull String[] args, @NotNull User sender, @NotNull Chat chat, int messageId) {
        SendMessage message = SendMessage.builder()
                .chatId(chat.getId())
                .text(Parser.all(sender, FinSchedule.getConfig().newUserStart))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();

        final var sent = SendingUtils.tryExecute(message);

        if (sent == null) {
            return;
        }

        this.menu.open(new MenuContext(chat, sender));
    }
}
