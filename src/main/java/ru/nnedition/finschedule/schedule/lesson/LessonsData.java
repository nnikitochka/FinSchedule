package ru.nnedition.finschedule.schedule.lesson;

import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.schedule.DataHandler;
import ru.nnedition.finschedule.schedule.groups.Group;
import ru.nnedition.finschedule.schedule.groups.GroupsData;
import ru.nnedition.finschedule.utils.DateUtils;
import ru.nnedition.logger.Logger;

import java.net.SocketException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LessonsData implements DataHandler {
    private static final Logger logger = Logger.getLogger(GroupsData.class);

    private final Map<String, Day> lessons = new ConcurrentHashMap<>();
    @NotNull
    public Map<String, Day> getLessons() {
        return this.lessons;
    }
    @Nullable
    public Day getLessons(@NotNull final String date) {
        return this.lessons.get(date);
    }

    private static final String SCHEDULE_SITE = "http://barnaul.fa.ru/lessons/";

    @Override
    public void updateData() {
        final var startDate = DateUtils.getCurrentDate();
        final var endDate = DateUtils.adjustDay(FinSchedule.getConfig().updateLessonsDaysCount);

        try {
            final var document = getDocument(startDate, endDate, null);
            handleDocument(document);
            return;
        } catch (Exception e) {
            logger.error("Ошибка получения полного расписания за один раз: "+e.getMessage()+". Приступаю к частичной обработке данных...");
        }

        //@TODO добавить автоматический баг репорт если дошло до этого момента

        final List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (final Group group : FinSchedule.getSchedule().getGroups()) {
            final var future = CompletableFuture.runAsync(() -> {
                try {
                    final var document = getDocument(startDate, endDate, group.name());
                    handleDocument(document);
                    logger.success("Группа "+group.name()+" была обработана.");
                } catch (Exception e) {
                    logger.error("Ошибка при обработке группы "+group.name()+": " + e.getMessage());
                }
            });

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> logger.info("Обработка групп окончена. Всего было обработано "+futures.size()+"."));
    }

    private void handleDocuments(Document... documents) {
        for (final Document document : documents) {
            this.handleDocument(document);
        }
    }

    private void handleDocument(Document document) {
        final var table = document.select(".tbl-lessons").get(1);
        if (table == null) return;

        final var rows = table.select("tr").stream().skip(1).toList();
        if (rows.isEmpty()) return;

        for (final Element row : rows) {
            final var cells = row.select("td");
            if (cells.size() < 9) {
                logger.error("Ошибка получения данных: "+cells);
                continue;
            }

            final var date = cells.getFirst().text().trim();
            final var dayOfWeek = cells.get(1).text().trim();
            final var groups = cells.get(2).text().trim().split(", ");
            final var groupRaw = groups[0];

            final var schedule = FinSchedule.getSchedule();
            final Group group = schedule.getGroupOrCreate(groupRaw);

            final var combined = Arrays.stream(groups).skip(0).map(schedule::getGroupOrCreate).toList();

            final var times = cells.get(3).text().trim().split(" - ");
            final var start = times[0];
            final var end = times[1];

            final var discipline = cells.get(4).text().trim();
            final var type = cells.get(5).text().trim();
            final var audience = cells.get(6).text().trim();
            final var buildingShort = cells.get(7).text().trim();
            final var building = schedule.getBuilding(buildingShort);
            final var teacher = cells.get(8).text().trim();

            final var lesson = new Lesson(
                    1,
                    start,
                    end,
                    discipline,
                    type,
                    audience,
                    building,
                    teacher,
                    combined
            );

            //System.out.println(lesson);
        }
    }

    /**
     * @param startDate Дата начала занятий.
     * @param endDate Дата окончания занятий.
     * @param group Группа для которой нужно получить расписание.
     * @return Объект Document с нужным не распаршеным расписанием. Может вернуть null если не получилось собрать данные.
     * Если параметр group равен null - вернётся расписание для всех групп.
     */
    @NotNull
    private Document getDocument(
            @NotNull final String startDate,
            @NotNull final String endDate,
            @Nullable final String group
    ) throws Exception {
        var formBodyBuilder = new FormBody.Builder()
                .add("Submit", "Просмотреть")
                .add("bdate", startDate)
                .add("edate", endDate);

        formBodyBuilder.add("groupname", group == null ? "?" : group);

        var request = new Request.Builder()
                .url(SCHEDULE_SITE)
                .post(formBodyBuilder.build())
                .build();

        var client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        //noinspection ConstantValue
        for (int attempt = 0; attempt < 3; attempt++) {
            try (var response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Неверный код ответа - " + response.code());
                }

                var body = response.body();

                if (body == null) throw new IllegalArgumentException("Пустое тело ответа.");

                var html = body.string();
                return Jsoup.parse(html);
            } catch (SocketException e) {
                logger.warn("SocketException: "+e.getMessage()+" on attempt "+(attempt + 1));
                if (attempt == 2) throw e;
            } catch (Exception e) {
                logger.warn("Ошибка при "+(attempt+1)+" попытке получении данных с сайта расписания: "+e.getMessage());
                if (attempt == 2) throw e;
            }
        }
        return null;
    }
}
