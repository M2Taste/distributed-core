package com.timotheus.core.redis;

import com.timotheus.core.cache.RedisPlayerDataCache;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.UUID;

public class RedisSubscriber {

    public RedisSubscriber(RedissonClient client,
                           RedisPlayerDataCache cache,
                           String localServerId,
                           JavaPlugin plugin) {

        RTopic topic = client.getTopic("player-data-update");
        plugin.getLogger().info("[PUBSUB][SUB] Listening on topic=player-data-update serverId=" + localServerId);

        topic.addListener(String.class, (channel, message) -> {
            String[] parts = message.split(":", 2);
            if (parts.length != 2) {
                plugin.getLogger().warning("[PUBSUB] Invalid message: " + message);
                return;
            }

            String sourceServerId = parts[0];
            if (localServerId.equals(sourceServerId)) {
                plugin.getLogger().info("[PUBSUB][SUB] Ignored self message source=" + sourceServerId + " payload=" + message);
                return;
            }

            UUID uuid;
            try {
                uuid = UUID.fromString(parts[1]);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("[PUBSUB] Invalid UUID in message: " + message);
                return;
            }

            plugin.getLogger().info(
                    "[PUBSUB][SUB] Received invalidation source=" + sourceServerId +
                    " target=" + localServerId +
                    " uuid=" + uuid
            );

            cache.remove(uuid);
            plugin.getLogger().info("[PUBSUB][SUB] Cache invalidated for uuid=" + uuid);
        });

    }
}
