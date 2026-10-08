package ru.nnedition.finschedule.placeholders.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.placeholders.Placeholder;
import ru.nnedition.finschedule.utils.Parser;

public class BotInfoPlaceholder extends Placeholder {
    public BotInfoPlaceholder() {
        super("bot_info");
    }

    @Override
    public String process(User user) {
        return Parser.placeholders(FinSchedule.getConfig().botInfo, user);
    }
}
