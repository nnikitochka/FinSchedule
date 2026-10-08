package ru.nnedition.finschedule.placeholders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.finschedule.placeholders.impl.AppVersionPlaceholder;
import ru.nnedition.finschedule.placeholders.impl.BotInfoPlaceholder;
import ru.nnedition.finschedule.placeholders.impl.BuildingsPlaceholder;
import ru.nnedition.finschedule.placeholders.impl.CommandsPlaceholder;
import ru.nnedition.logger.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderRegistry {
    private static final Logger logger = Logger.getLogger(PlaceholderRegistry.class);

    private final Map<String, Placeholder> placeholders = new HashMap<>();
    @NotNull
    public Collection<Placeholder> getPlaceholders() {
        return this.placeholders.values();
    }
    @Nullable
    public Placeholder getPlaceholder(@NotNull final String key) {
        return this.placeholders.get(key);
    }

    public void register(@NotNull final Placeholder placeholder) {
        if (this.placeholders.containsKey(placeholder.getKey())) {
            logger.error("Ошибка при регистрации команды: команда с именем " + placeholder.getKey() + " уже зарегистрирована!");
            return;
        }

        this.placeholders.put(placeholder.getKey(), placeholder);
    }
    public void register(@NotNull final Placeholder... placeholders) {
        for (final var command : placeholders) {
            this.register(command);
        }
    }

    public void registerDefaults() {
        this.register(
                new AppVersionPlaceholder(),
                new BotInfoPlaceholder(),
                new BuildingsPlaceholder(),
                new CommandsPlaceholder()
        );
    }
}
