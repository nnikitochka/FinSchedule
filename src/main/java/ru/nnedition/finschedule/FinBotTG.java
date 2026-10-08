package ru.nnedition.finschedule;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nnedition.finschedule.bot.TelegramBot;
import ru.nnedition.finschedule.bot.chat.ChatUtils;
import ru.nnedition.finschedule.bot.user.UserRepository;
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
        return this.adminListConfig.adminIds;
    }

    public FinBotTG(String token) {
        super(token);
        this.adminListConfig.load();
    }

    private final UserRepository usersManager = new UserRepository();
    @NotNull
    public UserRepository getUsersManager() {
        return this.usersManager;
    }

    public boolean isAdmin(User user) {
        return this.getAdmins().contains(user.getId().toString());
    }

    @Override
    public void register() throws TelegramApiException {
        super.register();
        this.refreshCommands();
    }

    @Override
    public void onCommandReceived(@NotNull final CommandReceiveEvent event) {
        this.usersManager.getOrLoad(event.getSender());

        final var label = event.getCommandLabel();
        final var command = this.getCommandRegistry().getCommand(label);
        if (command == null) return;

        final var chat = event.getChat();
        final var sender = event.getSender();

        {
            final Object senderIdentify = sender.getUserName() == null ? sender.getId() : sender.getUserName();
            final var chatType = ChatUtils.getChatType(chat);
            final var chatTypeTranslate = chatType == null ? "N/A" : chatType.translate.brief();

            @SuppressWarnings("StringBufferReplaceableByString")
            final var outputFormat = new StringBuilder()
                    .append(senderIdentify)
                    .append(" выполнил команду в ")
                    .append(chatTypeTranslate)
                    .append(": ")
                    .append(event.getText());

            System.out.println(outputFormat);
        }

        if (!event.getChat().isUserChat()) return;

        if (command.scope == CommandScope.ALL_ADMIN_PRIVATE_CHATS) {
            if (!FinSchedule.getBot().isAdmin(event.getSender())) return;
        }

        command.execute(event.getArgs(), sender, chat, event.getMessageId());
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceiveEvent event) {
        this.usersManager.getOrLoad(event.getSender());

        final var message = SendMessage.builder()
                .text(event.getText())
                .chatId(event.getChat().getId())
                .build();

        var a = SendingUtils.tryExecute(message);
    }

    @Override
    public void onCallbackQuery(@NotNull final CallbackQueryEvent event) {
        final var callback = event.getCallback();
        this.usersManager.getOrLoad(callback.getFrom());

        final var rawData = callback.getData();
        final var data = CallbackData.parse(rawData);

        final var handler = this.getCallbackHandlerRegistry().getHandler(data.key);
        if (handler == null) {
            logger.warn("Получен неизвестный коллбек ("+data.key+"): "+event.getCallback());
            return;
        }

        if (!(callback.getMessage() instanceof Message message)) {
            return;
        }

        handler.handle(data, callback.getId(), callback.getFrom(), message);
    }
}
