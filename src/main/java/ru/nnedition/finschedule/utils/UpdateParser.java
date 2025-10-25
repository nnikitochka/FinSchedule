package ru.nnedition.finschedule.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateParser {
    private static final Gson gson = new Gson();

    public static String parse(Update update) {
        String text = gson.toJson(update);
        System.out.println(text);
        return formatJson(text);
    }

    public static String formatJson(String input) {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .serializeSpecialFloatingPointValues()
                .create()
                .toJson(JsonParser.parseString(input));
    }

    public static void printFormated(Update update) {
        System.out.println(parse(update));
    }
}
