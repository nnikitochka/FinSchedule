package ru.nnedition.configuration.annotation;

import ru.nnedition.configuration.YamlConfig;
import ru.nnedition.logger.Logger;

import java.lang.reflect.Modifier;

public class ConfigAnnotationProcessor {
    private static final Logger log = Logger.getLogger(ConfigAnnotationProcessor.class);

    public static void process(final YamlConfig config) {
        var needSave = false;

        for (final var field : config.getClass().getDeclaredFields()) {
            final var annotation = field.getAnnotation(ConfigField.class);
            if (annotation == null) continue;

            if (Modifier.isFinal(field.getModifiers())) {
                log.warn("Field " + field.getName() + " is final");
                continue;
            }

            field.setAccessible(true);
            Object fieldValue;
            try {
                fieldValue = field.get(config);
            } catch (Throwable t) {
                log.error("Error receiving "+field.getName()+" field value: "+t.getLocalizedMessage(), t);
                continue;
            }

            final var section = annotation.section();
            if (section.isEmpty()) continue;
            var configValue = config.getYaml().get(section);

            if (configValue == null) {
                if (annotation.createIfExist() && fieldValue != null) {
                    try {
                        config.getYaml().set(section, fieldValue);
                    } catch (Throwable t) {
                        log.error("Error when writing value: "+t.getLocalizedMessage(), t);
                    }
                    needSave = true;
                }
                continue;
            }

            try {
                field.set(config, configValue);
            } catch (Throwable t) {
                log.error("Error writing value to "+field.getName()+" field: "+t.getLocalizedMessage(), t);
            }
        }

        if (needSave) config.save();
    }
}
