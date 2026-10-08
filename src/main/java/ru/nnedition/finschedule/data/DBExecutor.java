package ru.nnedition.finschedule.data;

import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jdbi.v3.core.extension.ExtensionConsumer;
import ru.nnedition.finschedule.data.dao.Dao;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class DBExecutor {

    public static <C extends Dao> void executeUpdate(Class<C> daoClass, Consumer<C> action) {
        executeUpdateAsync(daoClass, action).join();
    }

    public static <C extends Dao> CompletableFuture<Void> executeUpdateAsync(Class<C> daoClass, Consumer<C> action) {
        return DBManager.getJdbiExecutor()
                .useExtension(daoClass, (ExtensionConsumer<C, Exception>) action::accept)
                .exceptionally(DBExecutor::handleException)
                .toCompletableFuture();
    }


    public static <C extends Dao, R> R executeQuery(Class<C> daoClass, Function<C, R> action) {
        return executeQueryAsync(daoClass, action).join();
    }

    public static <C extends Dao, R> CompletableFuture<R> executeQueryAsync(Class<C> daoClass, Function<C, R> action) {
        return DBManager.getJdbiExecutor()
                .withExtension(daoClass, (ExtensionCallback<R, C, Exception>) action::apply)
                .exceptionally(DBExecutor::handleException)
                .toCompletableFuture();
    }

    private static <R> R handleException(Throwable t) {
        String text = t.getMessage();
//        boolean reportSent = text != null && FinBot.sendBugReport("Ошибка запроса к базе данных: " + text);
//        String reportStatus = reportSent ? "был успешно отправлен." : "не был отправлен из-за неизвестной ошибки.";
//        FinBot.logger.error(text + "\nОтчёт " + reportStatus);
        return null;
    }
}
