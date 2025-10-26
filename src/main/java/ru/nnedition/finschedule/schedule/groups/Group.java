package ru.nnedition.finschedule.schedule.groups;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Group(
        @NotNull String name,
        @NotNull ArrayList<Integer> subGroups
) {
    public Group(@NotNull String name) {
        this(name, new ArrayList<>());
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
