package ru.nnedition.finschedule;

import org.jetbrains.annotations.NotNull;
import ru.nnedition.finschedule.config.GeneralConfig;
import ru.nnedition.finschedule.config.SecretConfig;
import ru.nnedition.finschedule.schedule.Schedule;
import ru.nnedition.format.Plural;
import ru.nnedition.logger.Logger;

public final class FinSchedule {
    public static final Logger logger = Logger.getLogger(FinSchedule.class);

    private static final long START_TIME = System.currentTimeMillis();
    public static long getUpTime() {
        return System.currentTimeMillis() - START_TIME;
    }

    private static final SecretConfig secretConfig = new SecretConfig();
    private static final GeneralConfig generalConfig = new GeneralConfig();
    @NotNull
    public static GeneralConfig getConfig() {
        return generalConfig;
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
        generalConfig.load();

        schedule.loadData();

        var token = secretConfig.botToken;
        if (token == null || token.isEmpty()) {
            System.out.println(
                    "Похоже, программа запускается впервые.\n"+
                    "Введите токен бота в secret.yml в корневом каталоге."
            );
            return;
        }

        logger.info("Запуск бота...");

        bot = new FinBotTG(token);

        try {
            bot.getCallbackHandlerRegistry().registerDefaults();
            bot.getMenuRegistry().registerDefaults();
            bot.getCommandRegistry().registerDefaults();

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
