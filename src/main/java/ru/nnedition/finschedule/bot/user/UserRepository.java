package ru.nnedition.finschedule.bot.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.user.group.UserGroup;
import ru.nnedition.finschedule.data.DBExecutor;
import ru.nnedition.finschedule.data.dao.UserDao;
import ru.nnedition.logger.Logger;
import ru.nnedition.finschedule.schedule.groups.Group;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepository.class);

    private final Map<Long, BotUser> users = new HashMap<>();

    @Nullable
    public BotUser getUser(@NotNull final User user) {
        return this.users.get(user.getId());
    }
    @Nullable
    public BotUser getUser(final long userId) {
        return this.users.get(userId);
    }

    public void loadUsers() {
        this.users.clear();
    }

    @NotNull
    public BotUser getOrLoad(@NotNull final User user) {
        final var cached = this.users.get(user.getId());
        if (cached != null) {
            return cached;
        }

        final var resolved = this.loadOrCreate(user.getId());
        this.users.put(user.getId(), resolved);
        return resolved;
    }

    @NotNull
    private BotUser loadOrCreate(final long userId) {
        final var loaded = DBExecutor.executeQuery(UserDao.class, dao -> dao.getUserById(userId));

        if (loaded == null) {
            DBExecutor.executeUpdate(
                    UserDao.class,
                    dao -> dao.createUser(userId, String.valueOf(System.currentTimeMillis()), null, null)
            );

            return new BotUser(userId);
        }

        var groupName = loaded.getGroupName();
        if (groupName == null || groupName.isBlank()) {
//            groupName = DEFAULT_GROUP_NAME;
        }

        final var group = FinSchedule.getSchedule().getGroupOrCreate(groupName);
        return new BotUser(userId, null);
    }

    public void setUserGroup(@NotNull final User user, @NotNull final Group group) {
//        final var botUser = this.getOrLoad(user);
//        botUser.setGroup(group);
//        this.users.put(user.getId(), botUser);
//
//        DBExecutor.executeUpdate(
//                UserDao.class,
//                dao -> dao.updateUserGroup(user.getId(), group.name(), null)
//        );
    }
}
