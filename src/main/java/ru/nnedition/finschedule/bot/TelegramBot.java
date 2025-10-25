package ru.nnedition.finschedule.bot;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nnedition.finschedule.bot.callback.CallbackHandlerRegistry;
import ru.nnedition.finschedule.bot.command.CommandRegistry;
import ru.nnedition.finschedule.bot.event.CallbackQueryEvent;
import ru.nnedition.finschedule.bot.event.CommandReceiveEvent;
import ru.nnedition.finschedule.bot.event.MessageReceiveEvent;
import ru.nnedition.finschedule.bot.menu.MenuRegistry;
import ru.nnedition.finschedule.utils.SendingUtils;
import ru.nnedition.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class TelegramBot extends TelegramBotsLongPollingApplication {
    private static final Logger logger = Logger.getLogger(TelegramBot.class);

    private final String token;
    public final OkHttpTelegramClient httpClient;
    public TelegramBot(@NotNull final String token) {
        this.token = token;
        this.httpClient = new OkHttpTelegramClient(this.token);
    }

    private final UpdateConsumer updateConsumer = new UpdateConsumer(this);

    private final CommandRegistry commandRegistry = new CommandRegistry();
    @NotNull
    public final CommandRegistry getCommandRegistry() {
        return this.commandRegistry;
    }

    private final MenuRegistry menuRegistry = new MenuRegistry();
    @NotNull
    public final MenuRegistry getMenuRegistry() {
        return this.menuRegistry;
    }

    private final CallbackHandlerRegistry callbackHandlerRegistry = new CallbackHandlerRegistry();
    @NotNull
    public final CallbackHandlerRegistry getCallbackHandlerRegistry() {
        return this.callbackHandlerRegistry;
    }

    public void register() throws TelegramApiException {
        this.updateConsumer.start();
        try {
            super.registerBot(this.token, this.updateConsumer);
        } catch (TelegramApiException e) {
            this.updateConsumer.interrupt();
            throw e;
        }
    }

    public void unregister() throws TelegramApiException {
        super.unregisterBot(this.token);
        this.updateConsumer.interrupt();
    }

    public void refreshCommands() {
        for (final var entry : this.commandRegistry.groupByBotScopes().entrySet()) {
            final List<BotCommand> botCommands = new ArrayList<>();
            for (final var command : entry.getValue()) {
                if (!command.needRegister()) continue;
                botCommands.add(
                        BotCommand.builder()
                                .command(command.getLabel())
                                .description(command.getDescription())
                                .build()
                );
            }
            if (botCommands.isEmpty()) continue;

            SendingUtils.tryExecute(
                    SetMyCommands.builder().scope(entry.getKey()).commands(botCommands).build()
            );
        }
    }

    public abstract void onCommandReceived(@NotNull final CommandReceiveEvent event);
    public abstract void onMessageReceived(@NotNull final MessageReceiveEvent event);
    public abstract void onCallbackQuery(@NotNull final CallbackQueryEvent event);
}
