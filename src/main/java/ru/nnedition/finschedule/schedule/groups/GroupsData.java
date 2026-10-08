package ru.nnedition.finschedule.schedule.groups;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnedition.finschedule.schedule.DataHandler;
import ru.nnedition.logger.Logger;
import ru.nnedition.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GroupsData {
    private static final Logger logger = Logger.getLogger(GroupsData.class);

    private static final Pattern subGroupRegex = Pattern.compile("\\.\\d$");

    private final List<Group> groups = new ArrayList<>();

    @NotNull
    public List<Group> getGroups() {
        return this.groups;
    }
    @Nullable
    public Group getGroup(@NotNull final String name) {
        for (final Group group : this.groups)
            if (group.name().equals(name)) return group;
        return null;
    }
    public Group getOrCreate(@NotNull String name) {
        final var subGroupMatcher = subGroupRegex.matcher(name);
        Integer subGroupNum = null;

        if (subGroupMatcher.find()) {
            final var subGroupRaw = subGroupMatcher.group(0).substring(1);

            try {
                subGroupNum = Integer.parseInt(subGroupRaw);

                name = name.substring(0, name.length() - subGroupRaw.length() - 1);
            } catch (NumberFormatException e) {
                logger.error("Ошибка при обработки подгруппы "+name+", ожидалось число: \""+subGroupRaw+"\"");
            }
        }

        var group = getGroup(name);
        if (group == null) {
            group = new Group(name);
            this.groups.add(group);
        }

        if (subGroupNum != null && !group.subGroups().contains(subGroupNum)) {
            group.subGroups().add(subGroupNum);
        }

        return group;
    }
}
