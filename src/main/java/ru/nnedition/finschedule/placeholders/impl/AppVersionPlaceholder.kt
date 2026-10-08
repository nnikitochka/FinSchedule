package ru.nnedition.finschedule.placeholders.impl;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.nnedition.finschedule.ProjectInfo;
import ru.nnedition.finschedule.placeholders.Placeholder;

public class AppVersionPlaceholder extends Placeholder {
    public AppVersionPlaceholder() {
        super("app_version");
    }

    @Override
    public String process(User user) {
        return ProjectInfo.VERSION;
    }
}
