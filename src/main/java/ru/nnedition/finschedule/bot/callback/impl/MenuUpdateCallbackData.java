package ru.nnedition.finschedule.bot.callback.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.callback.CallbackData;
import ru.nnedition.finschedule.bot.menu.Menu;

import java.util.Map;

public class MenuUpdateCallbackData extends CallbackData {
    public MenuUpdateCallbackData(@NotNull final Menu menu) {
        super("menuUpdate", Map.of("menuId", menu.getId()));
    }

    @Nullable
    public static Menu getMenu(@NotNull final CallbackData data) {
        final var menuId = data.get("menuId");
        if (menuId == null) {
            throw new IllegalArgumentException("не найдено айди меню в данных коллбэка");
        }
        return FinSchedule.getBot().getMenuRegistry().getMenu(menuId);
    }
}
