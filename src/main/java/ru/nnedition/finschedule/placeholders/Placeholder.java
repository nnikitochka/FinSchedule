package ru.nnedition.finschedule.placeholders;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class Placeholder {
    @NotNull
    private final String key;
    public Placeholder(@NotNull final String key) {
        this.key = key;
    }

    @NotNull
    public final String getKey() {
        return this.key;
    }

    public abstract String process(User user);
}
