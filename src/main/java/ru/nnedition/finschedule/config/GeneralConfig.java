package ru.nnedition.finschedule.config;

import ru.nnedition.configuration.YamlConfig;
import ru.nnedition.configuration.annotation.ConfigField;

import java.util.List;

public final class GeneralConfig extends YamlConfig {
    public GeneralConfig() {
        super("config.yml");
    }

    @ConfigField(section = "update_lessons_days_count")
    public int updateLessonsDaysCount = 30;


    @ConfigField(section = "buttons.refresh")
    public String refreshButton = "🔄 Обновить";

    @ConfigField(section = "buttons.previous")
    public String prevButton = "⏪ Назад";

    @ConfigField(section = "buttons.next")
    public String nextButton = "⏩ Вперёд";

    @ConfigField(section = "buttons.close")
    public String closeButton = "🧨 Закрыть";

    @ConfigField(section = "buttons.back")
    public String backButton = "🚪 Обратно";


    @ConfigField(section = "format.building")
    public String buildingFormat = " » {short_name} - {full_name} ({address})";

    @ConfigField(section = "format.command")
    public String commandFormat = " » /{label} - {description}";


    @ConfigField(section = "messages.nothing_changed")
    public String nothingChanged = "😕 Ничего не поменялось...";

    @ConfigField(section = "messages.help")
    public String help = String.join("\n", List.of(
            "📒 *Команды бота:*",
            "%commands%",
            "",
            "🏘 *Список корпусов:*",
            "%buildings%"
    ));

    @ConfigField(section = "messages.bot_info")
    public String botInfo = String.join("\n", List.of(
            "🧐 *Насколько актуально расписание?*",
            " *»* Бот полностью автономен и берёт всю информацию с официального сайта расписания. Обновление расписания происходит каждые %schedule_update_delay% минут, поэтому оно всегда актуально! Кстати, последняя проверка расписания была %last_schedule_update% назад.",
            "",
            "🫠 *Есть ли тут расписание для моей группы?*",
            " *»* Если ваша группа есть на сайте расписания, то и тут она обязательно будет 😎",
            "",
            "❓ *Остались вопросы или есть проблемы?*",
            " *»* Если ответа на ваш вопрос нету в /help, то не стесняйтесь задавать ваши вопросы разработчику - @nnikitochka. Если же вы столкнулись с ошибкой или недоработкой, то вы можете сообщить о ней через команду /report для отправки автоматического отчёта об ошибке.",
            "",
            "😃 *Пишешь код на kotlin или java?*",
            " *»* Присоединяйся к разработке!",
            "",
            "🫀 Текущая версия: %app_version%",
            "👥 Сейчас ботом пользуются: %users_count%"
    ));

    @ConfigField(section = "messages.new_user_start")
    public String newUserStart = String.join("\n", List.of(
            "👋 Добро пожаловать! FinBot - это проект студента, созданный из чистого энтузиазма, работающий и постоянной улучшающийся с 14 ноября 2024 года.",
            "",
            "🙃 Перед началом вот немного полезной информации:",
            "",
            "%bot_info%"
    ));

    @ConfigField(section = "messages.select_group")
    public String selectGroup = "🧲 Выберите нужную группу.";
}
