package com.timotheus.core.service;

import com.timotheus.core.cache.RedisPlayerDataCache;
import com.timotheus.core.model.PlayerData;
import com.timotheus.core.redis.RedisPublisher;
import com.timotheus.core.repository.PlayerDataRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class PlayerService {

    private final RedisPlayerDataCache redisCache;
    private final PlayerDataRepository repository;
    private final RedisPublisher publisher;
    private final ExecutorService executor;
    private final JavaPlugin plugin;

    public PlayerService(RedisPlayerDataCache redisCache,
                         PlayerDataRepository repository,
                         RedisPublisher redisPublisher,
                         ExecutorService executor,
                         JavaPlugin plugin) {
        this.redisCache = redisCache;
        this.repository = repository;
        this.publisher = redisPublisher;
        this.executor = executor;
        this.plugin = plugin;
    }

    public void handlePlayerJoin(Player player) {
        UUID uuid = player.getUniqueId();

        CompletableFuture
                .supplyAsync(() -> {
                    PlayerData data = repository.incrementJoinsAndLoad(uuid);

                    redisCache.put(data);

                    publisher.publishPlayerUpdate(uuid);

                    return data.getJoins();
                }, executor)
                .thenAccept(joins -> Bukkit.getScheduler().runTask(plugin, () -> {
                    Player online = Bukkit.getPlayer(uuid);
                    if (online == null) {
                        return;
                    }

                    online.sendMessage(
                            ChatColor.GREEN + "Welcome! Joins: " + joins + " (Redis)"
                    );
                }))
                .exceptionally(throwable -> {
                    plugin.getLogger().severe(
                            "Failed to handle join for " + uuid + ": " + throwable.getMessage()
                    );
                    return null;
                });
    }

    public void handlePlayerQuit(Player player) {
        PlayerData data = redisCache.get(player.getUniqueId());
        if (data == null) {
            return;
        }

        CompletableFuture
                .runAsync(() -> repository.save(data), executor)
                .thenRun(() -> publisher.publishPlayerUpdate(player.getUniqueId()))
                .exceptionally(throwable -> {
                    plugin.getLogger().severe("Failed to persist quit data for " + player.getUniqueId() + ": " + throwable.getMessage());
                    return null;
                });
    }
}
