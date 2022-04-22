package com.jazzkuh.mttier.data.configuration;

import com.jazzkuh.mttier.Main;
import lombok.Getter;
import org.bukkit.Location;

public enum DefaultConfig {
    DATABASE_USE_SQLITE("database.use-sqlite", ValueType.BOOLEAN, "true"),
    DATABASE_IP("database.ip", ValueType.STRING, "127.0.0.1"),
    DATABASE_PORT("database.port", ValueType.INTEGER, "3306"),
    DATABASE_NAME("database.name", ValueType.STRING, "database"),
    DATABASE_USERNAME("database.username", ValueType.STRING, "username"),
    DATABASE_PASSWORD("database.password", ValueType.STRING, "password"),
    AUTO_UPDATE_TIER("settings.tier-update-automatically", ValueType.BOOLEAN, "true");

    private final @Getter String path;
    private final @Getter ValueType type;
    private final @Getter String value;
    private final @Getter Location location;

    DefaultConfig(String path, ValueType type, String value) {
        this.path = path;
        this.type = type;
        this.value = value;
        this.location = null;
    }

    DefaultConfig(String path, ValueType type, Location location) {
        this.path = path;
        this.type = type;
        this.value = null;
        this.location = location;
    }

    public String asString() {
        return Main.getInstance().getConfig().getString(this.getPath());
    }

    public Integer asInteger() {
        return Main.getInstance().getConfig().getInt(this.getPath());
    }

    public Boolean asBoolean() {
        return Main.getInstance().getConfig().getBoolean(this.getPath());
    }

    public static void init() {
        for (DefaultConfig configValue : DefaultConfig.values()) {
            if (Main.getInstance().getConfig().getString(configValue.getPath()) == null) {
                switch (configValue.getType()) {
                    case STRING: {
                        Main.getInstance().getConfig().set(configValue.getPath(), configValue.getValue());
                        break;
                    }
                    case BOOLEAN: {
                        Main.getInstance().getConfig().set(configValue.getPath(), Boolean.parseBoolean(configValue.getValue()));
                        break;
                    }
                    case INTEGER: {
                        Main.getInstance().getConfig().set(configValue.getPath(), Integer.parseInt(configValue.getValue()));
                        break;
                    }
                }
            }
        }

        Main.getInstance().saveConfig();
    }

    private enum ValueType {
        STRING, INTEGER, BOOLEAN
    }
}
