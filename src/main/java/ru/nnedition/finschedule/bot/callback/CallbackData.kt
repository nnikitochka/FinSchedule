package ru.nnedition.finschedule.bot.callback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CallbackData {
    @NotNull
    private final String key;
    @NotNull
    private final Map<String, String> context;

    public CallbackData(@NotNull final String key, @Nullable final Map<String, String> context) {
        this.key = key;
        this.context = context != null ? new HashMap<>(context) : new HashMap<>();
    }

    public CallbackData(@NotNull final String key) {
        this(key, null);
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    @NotNull
    public Map<String, String> getContext() {
        return new HashMap<>(this.context);
    }

    @Nullable
    public String get(@NotNull final String key) {
        return this.context.get(key);
    }


    private static final String KEY_SEPARATOR = ":";
    private static final String CONTEXT_SEPARATOR = "&";
    private static final String CONTEXT_EQUALIZER = "=";

    /**
     * Сериализует callback данные в строку формата: id:key1=value1&key2=value2
     */
    @NotNull
    public String serialize() {
        if (this.context.isEmpty()) {
            return this.key;
        }

        final var sb = new StringBuilder(this.key).append(KEY_SEPARATOR);
        this.context.forEach((key, value) -> {
            if (!sb.toString().endsWith(KEY_SEPARATOR)) {
                sb.append(CONTEXT_SEPARATOR);
            }
            sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                    .append(CONTEXT_EQUALIZER)
                    .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
        });

        return sb.toString();
    }

    /**
     * Десериализует строку в CallbackData
     */
    @NotNull
    public static CallbackData parse(final String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Callback data cannot be empty");
        }

        final String[] parts = data.split(KEY_SEPARATOR, 2);
        final String id = parts[0];
        final Map<String, String> context = new HashMap<>();

        if (parts.length > 1 && !parts[1].isEmpty()) {
            final String[] pairs = parts[1].split(CONTEXT_SEPARATOR);
            for (final var pair : pairs) {
                final String[] keyValue = pair.split(CONTEXT_EQUALIZER, 2);
                if (keyValue.length == 2) {
                    final String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    final String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    context.put(key, value);
                }
            }
        }

        return new CallbackData(id, context);
    }

    @Override
    public String toString() {
        return "CallbackData(" +
                "key=\"" + key + '\"' +
                ", context=" + context +
                ')';
    }
}
