package ru.nnedition.finschedule.bot.command.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.command.Command;
import ru.nnedition.finschedule.bot.command.CommandScope;
import ru.nnedition.finschedule.bot.menu.context.MenuContext;
import ru.nnedition.finschedule.bot.menu.admin.AdminMenu;

public class AdminCommand extends Command {
    public AdminCommand() {
        super("admin", "панель администратора", CommandScope.ALL_ADMIN_PRIVATE_CHATS);
    }

    private final AdminMenu adminMenu = FinSchedule.getBot().getMenuRegistry().getMenu(AdminMenu.class);
    @Override
    public void execute(@NotNull String[] args, @NotNull User sender, @NotNull Chat chat, int messageId) {
        MenuContext context = new MenuContext(chat, sender);

        this.adminMenu.open(context);
    }
}
