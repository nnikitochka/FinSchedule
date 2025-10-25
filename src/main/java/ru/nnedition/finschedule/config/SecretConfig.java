package ru.nnedition.finschedule.config;

import ru.nnedition.configuration.YamlConfig;
import ru.nnedition.configuration.annotation.ConfigField;

public class SecretConfig extends YamlConfig {
    public SecretConfig() {
        super("secret.yml");
    }

    @ConfigField(section = "token")
    public String botToken = "";
}
