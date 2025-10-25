package ru.nnedition.finschedule.schedule.buildings;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nnedition.finschedule.schedule.DataHandler;
import ru.nnedition.logger.Logger;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public final class BuildingsData implements DataHandler {
    private static final Logger logger = Logger.getLogger(BuildingsData.class);

    private final Map<String, Building> buildings = new HashMap<>();
    @NotNull
    public Collection<Building> getBuildings() {
        return this.buildings.values();
    }
    @Nullable
    public Building getByShortName(@NotNull final String shortName) {
        return this.buildings.get(shortName);
    }

    private static final Pattern BUILDINGS_REGEX = Pattern.compile("^(\\S+)\\s*-\\s*(.+?)\\s*\\((.+?)\\)$");
    private static final String SCHEDULE_SITE = "http://barnaul.fa.ru/lessons/";

    @Override
    public void updateData() {
        final var request = new Request.Builder().url(SCHEDULE_SITE).get().build();

        final String html;
        try (final var response = new OkHttpClient().newCall(request).execute()) {
            html = response.body() != null ? response.body().string() : null;
        } catch (IOException e) {
            logger.error("Ошибка при обновлении данных корпусов: "+e.getLocalizedMessage(), e);
            return;
        }

        if (html == null) return;

        final Map<String, Building> buildingMap = new HashMap<>();
        final Elements corpsTable = Jsoup.parse(html).select("table.simple");

        for (final Element element : corpsTable.select("tr td")) {
            final var corpsInfo = element.text();
            if (corpsInfo.isBlank()) continue;

            final var matcher = BUILDINGS_REGEX.matcher(corpsInfo);
            if (!matcher.find()) continue;

            final var shortName = matcher.group(1);

            buildingMap.put(shortName, new Building(shortName, matcher.group(2), matcher.group(3)));
        }

        this.buildings.clear();
        this.buildings.putAll(buildingMap);
    }
}
