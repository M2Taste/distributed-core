package com.timotheus.core.cache;

import com.timotheus.core.model.PlayerData;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataCache {

    private final ConcurrentHashMap<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    public PlayerData getOrCreate(UUID uuid) {
        return cache.computeIfAbsent(uuid, PlayerData::new);
    }

    public void remove(UUID uuid) {
        cache.remove(uuid);
    }
}