package ru.nnedition.finschedule.bot.event;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public final class CommandReceiveEvent extends MessageReceiveEvent {
    @NotNull
    private final String commandLabel;
    @NotNull
    private final String[] args;

    public CommandReceiveEvent(
            final int updateId,
            final int messageId,
            final long date,
            @NotNull final User sender,
            @NotNull final Chat chat,
            @NotNull final String text,
            @NotNull final String commandLabel,
            @NotNull final String[] args
    ) {
        super(updateId, messageId, date, sender, chat, text);
        this.commandLabel = commandLabel;
        this.args = args;
    }

    @NotNull
    public String getCommandLabel() {
        return this.commandLabel;
    }
    @NotNull
    public String[] getArgs() {
        return this.args;
    }

    @Override
    public String toString() {
        return "CommandReceiveEvent(" +
                "updateId=" + this.updateId +
                ", command=\"" + this.commandLabel + '\"' +
                ", args=[" + String.join(", ", this.args)+"]" +
                ", text=\"" + getText() + '\"' +
                ", chat=" + getChat() +
                ", sender=" + getSender() +
                ", date=" + getDate() +
                ", messageId=" + getMessageId() +
                ')';
    }
}
