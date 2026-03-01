package com.timotheus.core.cache;

import com.timotheus.core.model.PlayerData;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisPlayerDataCache {

    private final RMapCache<UUID, PlayerData> cache;

    public RedisPlayerDataCache(RedissonClient client) {
        this.cache = client.getMapCache("player-data");
    }

    public PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    public void put(PlayerData data) {
        cache.put(data.getUuid(), data, 10, TimeUnit.MINUTES);
    }

    public void remove(UUID uuid) {
        cache.remove(uuid);
    }
}