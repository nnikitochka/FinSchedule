package ru.nnedition.finschedule.bot.event;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public class MessageReceiveEvent extends Event {
    private final int messageId;
    private final long date;
    @NotNull
    private final User sender;
    @NotNull
    private final Chat chat;
    @NotNull
    private final String text;

    public MessageReceiveEvent(
            final int updateId,
            final int messageId,
            final long date,
            @NotNull final User sender,
            @NotNull final Chat chat,
            @NotNull final String text
    ) {
        super(updateId);
        this.messageId = messageId;
        this.date = date;
        this.sender = sender;
        this.chat = chat;
        this.text = text;
    }

    public int getMessageId() {
        return this.messageId;
    }
    public long getDate() {
        return this.date;
    }
    @NotNull
    public User getSender() {
        return this.sender;
    }
    @NotNull
    public Chat getChat() {
        return this.chat;
    }
    @NotNull
    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return "MessageReceiveEvent(" +
                "updateId=" + this.updateId +
                ", text=\"" + this.text + '\"' +
                ", chat=" + this.chat +
                ", sender=" + this.sender +
                ", date=" + this.date +
                ", messageId=" + this.messageId +
                ')';
    }
}
