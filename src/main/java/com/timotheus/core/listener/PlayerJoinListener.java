package com.timotheus.core.listener;

import com.timotheus.core.service.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PlayerService playerService;

    public PlayerJoinListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getLogger().fine("[DistributedCore] PlayerJoinEvent fired: " + event.getPlayer().getName());
        playerService.handlePlayerJoin(event.getPlayer());
    }
}
