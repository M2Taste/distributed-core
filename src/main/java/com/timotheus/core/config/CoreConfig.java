package com.timotheus.core.config;

import org.bukkit.plugin.java.JavaPlugin;

public class CoreConfig {

    private final JavaPlugin plugin;

    public CoreConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        plugin.reloadConfig();
    }

    public String getDatabaseHost() {
        return plugin.getConfig().getString("database.host");
    }
}