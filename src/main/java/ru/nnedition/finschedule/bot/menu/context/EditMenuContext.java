package ru.nnedition.finschedule.bot.menu.context;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public class EditMenuContext extends MenuContext {
    private final int messageId;

    public EditMenuContext(@NotNull final Chat chat, @NotNull final User sender, final int messageId) {
        super(chat, sender);
        this.messageId = messageId;
    }

    public int getMessageId() {
        return this.messageId;
    }
}
