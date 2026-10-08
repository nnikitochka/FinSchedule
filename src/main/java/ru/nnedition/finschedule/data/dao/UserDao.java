package ru.nnedition.finschedule.data.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jetbrains.annotations.Nullable;

public interface UserDao extends Dao {
    @SqlQuery("""
            SELECT
                user_id AS userId,
                login_date AS loginDate,
                group_name AS groupName,
                sub_group AS subGroup
            FROM users_data
            WHERE user_id = :userId
            LIMIT 1
            """)
    @RegisterBeanMapper(UserData.class)
    @Nullable
    UserData getUserById(@Bind("userId") long userId);

    @SqlUpdate("""
            INSERT OR IGNORE INTO users_data(user_id, login_date, group_name, sub_group)
            VALUES (:userId, :loginDate, :groupName, :subGroup)
            """)
    void createUser(
            @Bind("userId") long userId,
            @Bind("loginDate") String loginDate,
            @Bind("groupName") String groupName,
            @Bind("subGroup") Integer subGroup
    );

    @SqlUpdate("""
            UPDATE users_data
            SET group_name = :groupName,
                sub_group = :subGroup
            WHERE user_id = :userId
            """)
    void updateUserGroup(
            @Bind("userId") long userId,
            @Bind("groupName") String groupName,
            @Bind("subGroup") Integer subGroup
    );

    class UserData {
        private long userId;
        private String loginDate;
        private String groupName;
        private Integer subGroup;

        public long getUserId() {
            return this.userId;
        }
        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getLoginDate() {
            return this.loginDate;
        }
        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public String getGroupName() {
            return this.groupName;
        }
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public Integer getSubGroup() {
            return this.subGroup;
        }
        public void setSubGroup(Integer subGroup) {
            this.subGroup = subGroup;
        }
    }
}
