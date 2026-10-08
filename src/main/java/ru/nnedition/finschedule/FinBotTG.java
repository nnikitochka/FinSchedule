package ru.nnedition.finschedule;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;
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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public final class FinBotTG extends TelegramBot {
    private static final Logger logger = Logger.getLogger(FinBotTG.class);

    private final AdminList adminListConfig = new AdminList();
    @NotNull
    public List<String> getAdmins() {
        return this.adminListConfig.adminIds;
    }

    public FinBotTG(String token) {
        super(createHttpClient(), token);
        this.adminListConfig.load();
    }

    /**
     * Создаёт HTTP-клиент для общения с Telegram API.
     * Если в secret.yml включён прокси, он используется и для отправки запросов,
     * и для long-polling (getUpdates).
     */
    private static OkHttpClient createHttpClient() {
        final var secret = FinSchedule.getSecretConfig();

        final var defaultCreator = new TelegramOkHttpClientFactory.DefaultOkHttpClientCreator();
        if (!secret.proxyEnabled) {
            return defaultCreator.get();
        }
        if (!secret.isProxyConfigured()) {
            logger.warn("Прокси включён в secret.yml, но не заданы host или port. Бот запущен без прокси.");
            return defaultCreator.get();
        }

        final boolean socks = "socks".equalsIgnoreCase(secret.proxyType)
                || "socks5".equalsIgnoreCase(secret.proxyType);
        final var proxy = new Proxy(
                socks ? Proxy.Type.SOCKS : Proxy.Type.HTTP,
                new InetSocketAddress(secret.proxyHost, secret.proxyPort)
        );

        final boolean auth = secret.proxyUsername != null && !secret.proxyUsername.isEmpty();

        if (socks) {
            logger.info("Подключение к Telegram через SOCKS5-прокси " + secret.proxyHost + ":" + secret.proxyPort
                    + (auth ? " с авторизацией" : ""));

            if (auth) {
                // Для SOCKS учётные данные подставляет сам JVM через java.net.Authenticator
                // (OkHttp-ный proxyAuthenticator работает только для HTTP-прокси, код 407).
                // JDK при этом передаёт RequestorType.SERVER, поэтому фильтруем по порту прокси.
                final int proxyPort = secret.proxyPort;
                final String username = secret.proxyUsername;
                final String password = secret.proxyPassword == null ? "" : secret.proxyPassword;
                java.net.Authenticator.setDefault(new java.net.Authenticator() {
                    @Override
                    protected java.net.PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestingPort() != proxyPort) return null;
                        return new java.net.PasswordAuthentication(username, password.toCharArray());
                    }
                });
            }

            return new TelegramOkHttpClientFactory.SocksProxyOkHttpClientCreator(() -> proxy).get();
        }

        logger.info("Подключение к Telegram через HTTP-прокси " + secret.proxyHost + ":" + secret.proxyPort
                + (auth ? " с авторизацией" : ""));

        final Authenticator proxyAuthenticator = (route, response) -> {
            final String credential = Credentials.basic(
                    secret.proxyUsername,
                    secret.proxyPassword == null ? "" : secret.proxyPassword
            );
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        };

        return new TelegramOkHttpClientFactory.HttpProxyOkHttpClientCreator(
                () -> proxy,
                () -> auth ? proxyAuthenticator : null
        ).get();
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
