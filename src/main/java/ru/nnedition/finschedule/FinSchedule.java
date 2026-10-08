package ru.nnedition.finschedule;

import org.jetbrains.annotations.NotNull;
import ru.nnedition.finschedule.config.GeneralConfig;
import ru.nnedition.finschedule.config.SecretConfig;
import ru.nnedition.finschedule.data.DBManager;
import ru.nnedition.finschedule.schedule.Schedule;
import ru.nnedition.format.Plural;
import ru.nnedition.logger.Logger;

import java.io.IOException;

public final class FinSchedule {
    public static final Statistics statistics = new Statistics();

    public static final Logger logger = Logger.getLogger(FinSchedule.class);

    private static final SecretConfig secretConfig = new SecretConfig();
    private static final GeneralConfig generalConfig = new GeneralConfig();
    @NotNull
    public static GeneralConfig getConfig() {
        return generalConfig;
    }

    @NotNull
    public static SecretConfig getSecretConfig() {
        return secretConfig;
    }

    private static final Schedule schedule = new Schedule();
    @NotNull
    public static Schedule getSchedule() {
        return schedule;
    }

    private static FinBotTG bot;
    @NotNull
    public static FinBotTG getBot() {
        return bot;
    }

    static void main() {
        logger.info("Загрузка данных...");

        secretConfig.load();
        final var token = secretConfig.botToken;
        if (token == null || token.isEmpty()) {
            System.out.println(
                    "Похоже, программа запускается впервые.\n"+
                    "Введите токен бота в secret.yml в корневом каталоге."
            );
            return;
        }

        generalConfig.load();

        try {
            DBManager.initSQLite();
        } catch (IOException e) {
            logger.error("Ошибка загрузки базы данных: " + e.getLocalizedMessage(), e);
            logger.error("Остановка...");
            return;
        }

        schedule.loadData();

        logger.info("Запуск бота...");

        bot = new FinBotTG(token);

        try {
            bot.getCallbackHandlerRegistry().registerDefaults();
            bot.getMenuRegistry().registerDefaults();
            bot.getCommandRegistry().registerDefaults(bot);

            bot.register();
            logger.success("Бот был успешно зарегистрирован.");

            final var registeredHandlersFormated = new Plural("обработчик", "обработчика", "обработчиков", "")
                    .format(bot.getCallbackHandlerRegistry().getHandlers().size());
            logger.info("Зарегистрировано "+registeredHandlersFormated+" коллбеков.");

            final var registeredMenusFormated = new Plural("меню", "меню", "меню", "")
                    .format(bot.getMenuRegistry().getMenus().size());
            logger.info("Зарегистрировано "+registeredMenusFormated+".");

            final var registeredCommandsFormated = new Plural("команда", "команды", "команд", "")
                    .format(bot.getCommandRegistry().getCommands().size());
            logger.info("Зарегистрировано "+registeredCommandsFormated+".");

        } catch (Throwable t) {
            logger.error("Ошибка при регистрации бота: " + t.getLocalizedMessage(), t);
            return;
        }
    }

    public static void stop() {
        try {
            bot.unregister();
        } catch (Throwable t) {
            logger.error("Ошибка при остановке бота: " + t.getLocalizedMessage(), t);
        }
    }
}
