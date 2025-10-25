package ru.nnedition.finschedule;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nnedition.finschedule.bot.TelegramBot;
import ru.nnedition.finschedule.bot.callback.CallbackData;
import ru.nnedition.finschedule.bot.command.CommandScope;
import ru.nnedition.finschedule.bot.event.CallbackQueryEvent;
import ru.nnedition.finschedule.bot.event.CommandReceiveEvent;
import ru.nnedition.finschedule.bot.event.MessageReceiveEvent;
import ru.nnedition.finschedule.config.AdminList;
import ru.nnedition.finschedule.utils.SendingUtils;
import ru.nnedition.logger.Logger;

import java.util.List;

public final class FinBotTG extends TelegramBot {
    private static final Logger logger = Logger.getLogger(FinBotTG.class);

    private final AdminList adminListConfig = new AdminList();
    @NotNull
    public List<String> getAdmins() {
        return adminListConfig.adminIds;
    }

    public FinBotTG(String token) {
        super(token);
        this.adminListConfig.load();
    }

    public boolean isAdmin(User user) {
        return getAdmins().contains(user.getId().toString());
    }

    @Override
    public void register() throws TelegramApiException {
        super.register();
        this.refreshCommands();
    }

    @Override
    public void onCommandReceived(@NotNull final CommandReceiveEvent event) {
        final var label = event.getCommandLabel();
        var command = this.getCommandRegistry().getCommand(label);
        if (command == null) return;

        System.out.println(event.getCommandLabel()+": "+String.join(", ", event.getArgs()));

        if (!event.getChat().isUserChat()) return;

        if (command.getScope() == CommandScope.ALL_ADMIN_PRIVATE_CHATS) {
            if (!FinSchedule.getBot().isAdmin(event.getSender())) return;
        }

        command.execute(event.getArgs(), event.getSender(), event.getChat(), event.getMessageId());
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceiveEvent event) {
        var message = SendMessage.builder()
                .text(event.getText())
                .chatId(event.getChat().getId())
                .build();

        var a = SendingUtils.tryExecute(message);
    }

    @Override
    public void onCallbackQuery(@NotNull final CallbackQueryEvent event) {
        final var callback = event.getCallback();
        final var rawData = callback.getData();
        final var data = CallbackData.parse(rawData);

        final var handler = this.getCallbackHandlerRegistry().getHandler(data.getKey());
        if (handler == null) {
            logger.warn("Получен неизвестный коллбек: "+event.getCallback());
            return;
        }

        if (!(callback.getMessage() instanceof Message message)) {
            return;
        }

        handler.handle(data, callback.getId(), callback.getFrom(), message);
    }
}
