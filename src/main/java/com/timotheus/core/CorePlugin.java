package com.timotheus.core;

import com.timotheus.core.cache.RedisPlayerDataCache;
import com.timotheus.core.database.DatabaseManager;
import com.timotheus.core.listener.PlayerJoinListener;
import com.timotheus.core.listener.PlayerQuitListener;
import com.timotheus.core.redis.RedisManager;
import com.timotheus.core.repository.MySQLPlayerDataRepository;
import com.timotheus.core.repository.PlayerDataRepository;
import com.timotheus.core.service.PlayerService;
import com.timotheus.core.util.ExecutorProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CorePlugin extends JavaPlugin {

    private ExecutorProvider executorProvider;
    private DatabaseManager databaseManager;
    private RedisManager redisManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            this.executorProvider = new ExecutorProvider();

            String dbHost = getConfig().getString("database.host", "localhost");
            int dbPort = getConfig().getInt("database.port", 3306);
            String dbName = getConfig().getString("database.name", "core");
            String dbUser = getConfig().getString("database.user", "root");
            String dbPassword = getConfig().getString("database.password", "root");

            this.databaseManager = new DatabaseManager(
                    dbHost,
                    dbPort,
                    dbName,
                    dbUser,
                    dbPassword
            );

            String redisHost = getConfig().getString("redis.host", "localhost");
            int redisPort = getConfig().getInt("redis.port", 6379);
            this.redisManager = new RedisManager(redisHost, redisPort);

            RedisPlayerDataCache redisCache = new RedisPlayerDataCache(redisManager.getClient());
            PlayerDataRepository repository = new MySQLPlayerDataRepository(databaseManager.getDataSource());

            PlayerService playerService = new PlayerService(
                    redisCache,
                    repository,
                    executorProvider.getExecutor(),
                    this
            );

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
        if (redisManager != null) {
            redisManager.shutdown();
        }
        getLogger().info("DistributedCore disabled.");
    }
}
