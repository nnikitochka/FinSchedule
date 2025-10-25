package ru.nnedition.finschedule.bot.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.finschedule.bot.menu.admin.AdminMenu;
import ru.nnedition.logger.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MenuRegistry {
    private static final Logger logger = Logger.getLogger(MenuRegistry.class);

    private final Map<String, Menu> menus = new HashMap<>();
    @NotNull
    public Collection<Menu> getMenus() {
        return this.menus.values();
    }
    @Nullable
    public Menu getMenu(final @NotNull String id) {
        return this.menus.get(id);
    }
    @SuppressWarnings("unchecked")
    @NotNull
    public <M extends Menu> M getMenu(final Class<M> menuClass) {
        return (M) this.menus.get(menuClass.getSimpleName());
    }

    public void register(final @NotNull Menu menu) {
        if (this.menus.containsKey(menu.getId())) {
            logger.error("Ошибка при регистрации меню: меню " + menu.getClass().getSimpleName() + " уже зарегистрировано!");
            return;
        }

        this.menus.put(menu.getId(), menu);
    }
    public void register(final Menu... menus) {
        for (final var menu : menus) {
            this.register(menu);
        }
    }

    public void registerDefaults() {
        this.register(
                new AdminMenu()
        );
    }
}
