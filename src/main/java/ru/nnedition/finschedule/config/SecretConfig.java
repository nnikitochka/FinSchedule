package ru.nnedition.finschedule.config;

import ru.nnedition.configuration.YamlConfig;
import ru.nnedition.configuration.annotation.ConfigField;

public class SecretConfig extends YamlConfig {
    public SecretConfig() {
        super("secret.yml");
    }

    @ConfigField(section = "token")
    public String botToken = "";

    @ConfigField(section = "proxy.enabled")
    public boolean proxyEnabled = false;

    /** Тип прокси: "http" или "socks5". */
    @ConfigField(section = "proxy.type")
    public String proxyType = "http";

    @ConfigField(section = "proxy.host")
    public String proxyHost = "";

    @ConfigField(section = "proxy.port")
    public int proxyPort = 8080;

    @ConfigField(section = "proxy.username")
    public String proxyUsername = "";

    @ConfigField(section = "proxy.password")
    public String proxyPassword = "";

    public boolean isProxyConfigured() {
        return this.proxyEnabled
                && this.proxyHost != null && !this.proxyHost.isEmpty()
                && this.proxyPort > 0 && this.proxyPort <= 65535;
    }
}
