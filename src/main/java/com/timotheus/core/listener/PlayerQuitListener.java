package com.timotheus.core.listener;

import com.timotheus.core.service.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PlayerService playerService;

    public PlayerQuitListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerService.handlePlayerQuit(event.getPlayer());
    }
}
