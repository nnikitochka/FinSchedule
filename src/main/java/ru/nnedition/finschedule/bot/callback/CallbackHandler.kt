package ru.nnedition.finschedule.bot.callback;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.function.Consumer;

public abstract class CallbackHandler {
    @NotNull
    private final String key;

    public CallbackHandler(@NotNull final String key) {
        this.key = key;
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    public abstract void handle(CallbackData data, String callbackId, User from, Message message);
}
