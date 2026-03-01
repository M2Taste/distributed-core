package com.timotheus.core;

import com.timotheus.core.cache.PlayerDataCache;
import com.timotheus.core.database.DatabaseManager;
import com.timotheus.core.listener.PlayerJoinListener;
import com.timotheus.core.listener.PlayerQuitListener;
import com.timotheus.core.repository.MySQLPlayerDataRepository;
import com.timotheus.core.repository.PlayerDataRepository;
import com.timotheus.core.service.PlayerService;
import com.timotheus.core.util.ExecutorProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CorePlugin extends JavaPlugin {

    private ExecutorProvider executorProvider;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            // Infrastructure
            this.executorProvider = new ExecutorProvider();

            String host = getConfig().getString("database.host", "localhost");
            int port = getConfig().getInt("database.port", 3306);
            String dbName = getConfig().getString("database.name", "core");
            String user = getConfig().getString("database.user", "root");
            String password = getConfig().getString("database.password", "root");

            this.databaseManager = new DatabaseManager(
                    host,
                    port,
                    dbName,
                    user,
                    password
            );

            // Data layer
            PlayerDataCache cache = new PlayerDataCache();
            PlayerDataRepository repository =
                    new MySQLPlayerDataRepository(databaseManager.getDataSource());

            // Service layer
            PlayerService playerService = new PlayerService(
                    cache,
                    repository,
                    executorProvider.getExecutor(),
                    this
            );

            // Listener
            getServer().getPluginManager().registerEvents(
                    new PlayerJoinListener(playerService),
                    this
            );
            getServer().getPluginManager().registerEvents(
                    new PlayerQuitListener(playerService),
                    this
            );

            getLogger().info("DistributedCore enabled.");
        } catch (Throwable t) {
            getLogger().log(Level.SEVERE, "DistributedCore failed to enable.", t);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (executorProvider != null) {
            executorProvider.shutdown();
        }
        if (databaseManager != null) {
            databaseManager.shutdown();
        }
        getLogger().info("DistributedCore disabled.");
    }
}
