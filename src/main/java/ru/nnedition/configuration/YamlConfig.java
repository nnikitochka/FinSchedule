package ru.nnedition.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nnedition.configuration.annotation.ConfigAnnotationProcessor;
import ru.nnedition.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlConfig {
    protected static final Logger logger = LoggerFactory.getLogger(YamlConfig.class.getSimpleName());

    private final File configFile;
    public YamlConfig(final String fileName) {
        this.configFile = new File(fileName);
    }
    public YamlConfig(final File file) {
        this.configFile = file;
    }

    protected YamlConfiguration yaml;
    public YamlConfiguration getYaml() {return yaml;}

    public final void load() {
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch (Exception ex) {
                logger.error("Error creating "+this.configFile.getName()+" configuration file: "+ex.getLocalizedMessage(), ex);
            }
        }

        this.yaml = new YamlConfiguration();

        try {
            this.yaml.options().parseComments(true);
            this.yaml.load(this.configFile);
        } catch (Exception ex) {
            logger.error("Error loading the "+this.configFile.getName()+" configuration file: "+ex.getLocalizedMessage(), ex);
        }

        ConfigAnnotationProcessor.process(this);

        this.save();
        this.onLoad();
    }

    public void onLoad() {}

    public final void save() {
        try {
            this.yaml.save(this.configFile);
        } catch (Exception ex) {
            logger.error("Error saving the "+this.configFile.getName()+" configuration file: "+ex.getLocalizedMessage(), ex);
        }
    }

    public final void set(final String key, final Object value) {
        this.yaml.set(key, value);
        this.save();
    }
}
