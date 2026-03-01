package com.timotheus.core.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisManager {

    private final RedissonClient client;

    public RedisManager(String host, int port) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port);

        this.client = Redisson.create(config);
    }

    public RedissonClient getClient() {
        return client;
    }

    public void shutdown() {
        client.shutdown();
    }
}