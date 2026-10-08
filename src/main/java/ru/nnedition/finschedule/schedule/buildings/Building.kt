package ru.nnedition.finschedule.schedule.buildings;

import org.jetbrains.annotations.NotNull;
import ru.nnedition.finschedule.FinSchedule;

public record Building(
        @NotNull String shortName,
        @NotNull String fullName,
        @NotNull String address
) {
    @NotNull
    public String format() {
        return FinSchedule.getConfig().buildingFormat
                .replace("{short_name}", this.shortName)
                .replace("{full_name}", this.fullName)
                .replace("{address}", this.address);
    }
}
