package ru.nnedition.finschedule.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.logger.Logger;

import java.io.Serializable;

public class SendingUtils {
    private static final Logger logger = Logger.getLogger(SendingUtils.class);

    @Nullable
    public static <T extends Serializable, M extends BotApiMethod<T>> T tryExecute(
            @NotNull final M method,
            @NotNull final TelegramClient client
    ) {
        try {
            return client.execute(method);
        } catch (Exception e) {
            logger.error("Ошибка при выполнении метода " + method.getMethod() + ": " + e.getMessage(), e);
            return null;
        }
    }

    @Nullable
    public static <T extends Serializable, M extends BotApiMethod<T>> T tryExecute(@NotNull final M method) {
        return tryExecute(method, FinSchedule.getBot().httpClient);
    }

    /**
     * @param method Метод изменения текста.
     * @param outputNotModified Нужно ли выводить лог о том, что сообщение не было изменено из-за одинакового содержимого.
     * @param client Телеграмм клиент.
     * @return true - если сообщение было успешно изменено, false - в обратном случае.
     */
    public static boolean tryEdit(
            @NotNull final EditMessageText method,
            final boolean outputNotModified,
            @NotNull final TelegramClient client
    ) {
        try {
            client.execute(method);
            return true;
        } catch (TelegramApiRequestException e) {
            if (e.getErrorCode() == 400 && e.getApiResponse().contains("message is not modified")) {
                if (outputNotModified)
                    logger.error("Ошибка при выполнении метода " + method.getMethod() + ": " + e.getMessage(), e);
            }
            else logger.error("Ошибка при выполнении метода " + method.getMethod() + ": " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Ошибка при выполнении метода " + method.getMethod() + ": " + e.getMessage(), e);
            return false;
        }
    }
    public static boolean tryEdit(@NotNull final EditMessageText method, final boolean outputNotModified) {
        return tryEdit(method, outputNotModified, FinSchedule.getBot().httpClient);
    }
    public static boolean tryEdit(@NotNull final EditMessageText method) {
        return tryEdit(method, false, FinSchedule.getBot().httpClient);
    }
}
