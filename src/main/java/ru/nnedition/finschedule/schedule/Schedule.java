package ru.nnedition.finschedule.schedule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.finschedule.schedule.buildings.Building;
import ru.nnedition.finschedule.schedule.buildings.BuildingsData;
import ru.nnedition.finschedule.schedule.groups.Group;
import ru.nnedition.finschedule.schedule.groups.GroupsData;
import ru.nnedition.finschedule.schedule.lesson.Day;
import ru.nnedition.finschedule.schedule.lesson.Lesson;
import ru.nnedition.finschedule.schedule.lesson.LessonsData;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class Schedule {
    public void loadData() {
        this.updateBuildingsData();
        this.updateGroupsData();
        this.updateLessonsData();
    }


    private final BuildingsData buildingsData = new BuildingsData();
    @NotNull
    public Collection<Building> getBuildings() {
        return this.buildingsData.getBuildings();
    }
    @Nullable
    public Building getBuilding(String shortName) {
        return this.buildingsData.getByShortName(shortName);
    }
    public void updateBuildingsData() {
        this.buildingsData.updateData();
    }


    private final GroupsData groupsData = new GroupsData();
    @NotNull
    public List<Group> getGroups() {
        return this.groupsData.getGroups();
    }
    @Nullable
    public Group getGroup(@NotNull final String name) {
        return this.groupsData.getGroup(name);
    }
    @NotNull
    public Group getGroupOrCreate(@NotNull final String name) {
        return this.groupsData.getOrCreate(name);
    }
    public void updateGroupsData() {
        this.groupsData.updateData();
    }


    private final LessonsData lessonsData = new LessonsData();
    @NotNull
    public Map<String, Day> getLessons() {
        return this.lessonsData.getLessons();
    }
    public void updateLessonsData() {
        this.lessonsData.updateData();
    }
}
