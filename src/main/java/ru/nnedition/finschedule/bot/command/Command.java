package ru.nnedition.finschedule.bot.command;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public abstract class Command {
    @NotNull
    private final String label;
    @NotNull
    private final String description;
    @NotNull
    private final CommandScope scope;
    private final boolean register;

    /**
     * @param label Название команды
     * @param description Описание команды
     * @param scope В каких чатах разрешено использовать команды, по совместительству используется для регистрации
     * @param register Нужно ли регистрировать команду
     */
    public Command(
            @NotNull final String label,
            @NotNull final String description,
            @NotNull final CommandScope scope,
            final boolean register
    ) {
        this.label = label;
        this.description = description;
        this.scope = scope;
        this.register = register;
    }

    public Command(@NotNull final String label, @NotNull final String description, @NotNull final CommandScope scope) {
        this(label, description, scope, true);
    }

    public Command(@NotNull final String label, @NotNull final String description, final boolean register) {
        this(label, description, CommandScope.ALL_PRIVATE_CHATS, register);
    }

    public Command(@NotNull final String label, @NotNull final String description) {
        this(label, description, CommandScope.ALL_PRIVATE_CHATS, true);
    }

    @NotNull
    public final String getLabel() {
        return this.label;
    }
    @NotNull
    public final String getDescription() {
        return this.description;
    }
    @NotNull
    public final CommandScope getScope() {
        return this.scope;
    }
    public final boolean needRegister() {
        return this.register;
    }

    public abstract void execute(
            @NotNull final String[] args,
            @NotNull final User sender,
            @NotNull final Chat chat,
            final int messageId
    );
}
