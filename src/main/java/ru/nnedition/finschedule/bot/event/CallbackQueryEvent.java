package ru.nnedition.finschedule.bot.event;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

public final class CallbackQueryEvent extends Event {
    @NotNull
    private final CallbackQuery callback;

    public CallbackQueryEvent(
            final int updateId,
            @NotNull final CallbackQuery callback
    ) {
        super(updateId);
        this.callback = callback;
    }

    @NotNull
    public CallbackQuery getCallback() {
        return this.callback;
    }

    @Override
    public String toString() {
        return "CallbackQueryEvent{" +
                "callback=" + this.callback +
                ", updateId=" + this.updateId +
                '}';
    }
}
