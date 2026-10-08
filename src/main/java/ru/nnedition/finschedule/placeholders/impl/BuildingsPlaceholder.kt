package ru.nnedition.finschedule.placeholders.impl;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.placeholders.Placeholder;
import ru.nnedition.finschedule.schedule.buildings.Building;
import ru.nnedition.utils.StringUtils;

public class BuildingsPlaceholder extends Placeholder {
    public BuildingsPlaceholder() {
        super("buildings");
    }

    @Override
    public String process(User user) {
        final var builder = new StringBuilder();
        for (final var building : FinSchedule.getSchedule().getBuildings()) {
            builder.append(building.format()).append('\n');
        }
        return StringUtils.trimEnd(builder.toString());
    }
}
