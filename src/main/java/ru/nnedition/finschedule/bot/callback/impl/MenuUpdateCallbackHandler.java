package ru.nnedition.finschedule.bot.callback.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.callback.CallbackData;
import ru.nnedition.finschedule.bot.callback.CallbackHandler;
import ru.nnedition.finschedule.bot.menu.context.EditMenuContext;
import ru.nnedition.finschedule.utils.SendingUtils;

public class MenuUpdateCallbackHandler extends CallbackHandler {
    public MenuUpdateCallbackHandler() {
        super("menuUpdate");
    }

    @Override
    public void handle(CallbackData data, String callbackId, User from, Message message) {
        final var menuId = data.get("menuId");
        if (menuId == null) return;
        final var menu = FinSchedule.getBot().getMenuRegistry().getMenu(menuId);
        if (menu == null) return;

        final var isSuccess = menu.update(new EditMenuContext(message.getChat(), from, message.getMessageId()));

        if (isSuccess) return;

        final var answer = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackId)
                .text(FinSchedule.getConfig().nothingChanged)
                .build();

        SendingUtils.tryExecute(answer);
    }
}
