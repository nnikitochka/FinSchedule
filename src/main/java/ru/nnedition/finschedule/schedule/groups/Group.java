package ru.nnedition.finschedule.schedule.groups;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Group(
        @NotNull String name,
        @NotNull List<Integer> subGroups
) {
    public Group(@NotNull String name) {
        this(name, List.of());
    }

    @NotNull
    @Override
    public String toString() {
        return "Group(" +
                "name='" + name + '\'' +
                ", subGroups=" + subGroups +
                ')';
    }
}
