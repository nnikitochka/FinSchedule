package ru.nnedition.finschedule.bot.menu.context;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public class MenuContext {
    @NotNull
    private final Chat chat;
    @NotNull
    private final User sender;

    public MenuContext(@NotNull final Chat chat, @NotNull final User sender) {
        this.chat = chat;
        this.sender = sender;
    }

    @NotNull
    public final Chat getChat() { return this.chat; }
    @NotNull
    public final User getSender() { return this.sender; }
}
