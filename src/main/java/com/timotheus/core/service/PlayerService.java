package com.timotheus.core.service;

import com.timotheus.core.cache.PlayerDataCache;
import com.timotheus.core.model.PlayerData;
import com.timotheus.core.repository.PlayerDataRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class PlayerService {

    private final PlayerDataCache cache;
    private final PlayerDataRepository repository;
    private final ExecutorService executor;
    private final JavaPlugin plugin;

    public PlayerService(PlayerDataCache cache,
                         PlayerDataRepository repository,
                         ExecutorService executor,
                         JavaPlugin plugin) {
        this.cache = cache;
        this.repository = repository;
        this.executor = executor;
        this.plugin = plugin;
    }

    public void handlePlayerJoin(Player player) {
        UUID uuid = player.getUniqueId();

        CompletableFuture
                .supplyAsync(() ->
                        repository.load(uuid)
                                .orElseGet(() -> {
                                    PlayerData data = new PlayerData(uuid);
                                    repository.save(data);
                                    return data;
                                }), executor
                )
                .thenAccept(data -> Bukkit.getScheduler().runTask(plugin, () -> {

                    Player onlinePlayer = Bukkit.getPlayer(uuid);
                    if (onlinePlayer == null) {
                        return; // Spieler nicht mehr online
                    }

                    // Cache übernimmt die DB-Instanz
                    cache.put(data);

                    data.incrementJoins();

                    onlinePlayer.sendMessage(
                            "§aLoaded from DB | Joins: " + data.getJoins()
                    );
                }));
    }

    public void handlePlayerQuit(Player player) {
        PlayerData data = cache.get(player.getUniqueId());
        if (data != null) {
            CompletableFuture.runAsync(
                    () -> repository.save(data),
                    executor
            );
            cache.remove(player.getUniqueId());
        }
    }
}