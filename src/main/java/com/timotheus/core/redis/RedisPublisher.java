package com.timotheus.core.redis;

import org.redisson.api.RedissonClient;
import org.redisson.api.RTopic;

import java.util.UUID;
import java.util.logging.Logger;

public class RedisPublisher {

    private final RTopic topic;
    private final String serverId;
    private final Logger logger;

    public RedisPublisher(RedissonClient client, String serverId, Logger logger) {
        this.topic = client.getTopic("player-data-update");
        this.serverId = serverId;
        this.logger = logger;
    }

    public void publishPlayerUpdate(UUID uuid) {
        String payload = serverId + ":" + uuid;
        topic.publish(payload);
        logger.info("[PUBSUB][PUB] Sent invalidation payload=" + payload);
    }
}
