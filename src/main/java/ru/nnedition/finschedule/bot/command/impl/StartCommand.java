package ru.nnedition.finschedule.bot.command.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.command.Command;
import ru.nnedition.finschedule.utils.Parser;
import ru.nnedition.finschedule.utils.SendingUtils;

import java.util.List;

public class StartCommand extends Command {
    public StartCommand() {
        super("start", "начать", true);
    }

    @Override
    public void execute(@NotNull String[] args, @NotNull User sender, @NotNull Chat chat, int messageId) {
        SendMessage message = SendMessage.builder()
                .chatId(chat.getId())
                .text(Parser.all(sender, FinSchedule.getConfig().newUserStart))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();

        SendingUtils.tryExecute(message);
    }
}
