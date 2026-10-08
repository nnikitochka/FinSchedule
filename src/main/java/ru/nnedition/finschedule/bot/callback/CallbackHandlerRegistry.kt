package ru.nnedition.finschedule.bot.callback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.finschedule.bot.callback.impl.MenuUpdateCallbackHandler;
import ru.nnedition.logger.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CallbackHandlerRegistry {
    private static final Logger logger = Logger.getLogger(CallbackHandlerRegistry.class);

    private final Map<String, CallbackHandler> handlers = new HashMap<>();
    @NotNull
    public Collection<CallbackHandler> getHandlers() {
        return this.handlers.values();
    }
    @Nullable
    public CallbackHandler getHandler(@NotNull final String key) {
        return this.handlers.get(key);
    }

    public void register(@NotNull final CallbackHandler handler) {
        if (this.handlers.containsKey(handler.getKey())) {
            logger.error("Ошибка при регистрации обработчика коллбэка: обработчик с ключом " + handler.getKey() + " уже зарегистрирован!");
            return;
        }

        this.handlers.put(handler.getKey(), handler);
    }
    public void register(@NotNull final CallbackHandler... handlers) {
        for (final var handler : handlers) {
            this.register(handler);
        }
    }

    public void unregister(@NotNull final CallbackHandler handler) {
        this.unregister(handler.getKey());
    }
    public void unregister(@NotNull final String key) {
        this.handlers.remove(key);
    }

    public void registerDefaults() {
        this.register(
                new MenuUpdateCallbackHandler()
        );
    }
}
