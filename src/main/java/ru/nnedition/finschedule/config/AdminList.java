package ru.nnedition.finschedule.config;

import ru.nnedition.configuration.YamlConfig;
import ru.nnedition.configuration.annotation.ConfigField;

import java.util.List;

public class AdminList extends YamlConfig {
    public AdminList() {
        super("admins.yml");
    }

    @ConfigField(section = "ids")
    public List<String> adminIds = List.of("1606578117");
}
