package ru.nnedition.finschedule.bot.menu;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import ru.nnedition.finschedule.bot.menu.context.EditMenuContext;
import ru.nnedition.finschedule.bot.menu.context.MenuContext;

public class Menu {
    protected String id = this.getClass().getSimpleName();
    @NotNull
    public final String getId() {
        return this.id;
    }

    public boolean open(MenuContext context) {
        return false;
    }

    public boolean edit(EditMenuContext context) {
        return false;
    }

    public boolean update(EditMenuContext context) {
        return false;
    }
}
