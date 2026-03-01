package com.timotheus.core.service;

import com.timotheus.core.cache.PlayerDataCache;
import com.timotheus.core.model.PlayerData;
import org.bukkit.entity.Player;

public class PlayerService {

    private final PlayerDataCache cache;

    public PlayerService(PlayerDataCache cache) {
        this.cache = cache;
    }

    public void handlePlayerJoin(Player player) {
        PlayerData data = cache.getOrCreate(player.getUniqueId());
        data.incrementJoins();

        player.sendMessage(
                "§aWelcome " + player.getName() +
                        " | Joins: " + data.getJoins() +
                        " | Coins: " + data.getCoins()
        );
    }

    public void handlePlayerQuit(Player player) {
        // später: save to DB
        cache.remove(player.getUniqueId());
    }
}