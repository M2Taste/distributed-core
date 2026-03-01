package com.timotheus.core;

import com.timotheus.core.cache.PlayerDataCache;
import com.timotheus.core.listener.PlayerJoinListener;
import com.timotheus.core.service.PlayerService;
import com.timotheus.core.util.ExecutorProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CorePlugin extends JavaPlugin {

    private ExecutorProvider executorProvider;
    private PlayerService playerService;

    @Override
    public void onEnable() {
        this.executorProvider = new ExecutorProvider();

        PlayerDataCache cache = new PlayerDataCache();
        this.playerService = new PlayerService(cache);

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(playerService),
                this
        );

        getLogger().info("DistributedCore enabled.");
    }

    @Override
    public void onDisable() {
        executorProvider.shutdown();
        getLogger().info("DistributedCore disabled.");
    }
}