package ru.nnedition.finschedule.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.cache.caffeine.CaffeineCachePlugin;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.async.JdbiExecutor;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

public class DBManager {
    private static Jdbi jdbi;
    @NotNull
    public static Jdbi getJdbi() {
        return jdbi;
    }
    private static JdbiExecutor jdbiExecutor;
    @NotNull
    public static JdbiExecutor getJdbiExecutor() {
        return jdbiExecutor;
    }

    public static void initSQLite() throws IOException {
        final var db = new File("data.db");
        if (!db.exists()) {
            db.createNewFile();
        }

        final var config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setConnectionTestQuery("SELECT 1");
        config.setJdbcUrl("jdbc:sqlite:" + db);

        final var dataSource = new HikariDataSource(config);

        jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new CaffeineCachePlugin());
        jdbi.installPlugin(new SqlObjectPlugin());

        try (final var inStr = DBManager.class.getResourceAsStream("/database.sql")) {
            if (inStr == null) {
                throw new IOException("файл database.sql базы данных не был найден в ресурсах приложения.");
            }
            jdbi.useHandle((handle) ->
                    handle.createScript(new String(inStr.readAllBytes())).execute()
            );
        }

        final var executor = Executors.newFixedThreadPool(8);
        jdbiExecutor = JdbiExecutor.create(jdbi, executor);
    }
}
