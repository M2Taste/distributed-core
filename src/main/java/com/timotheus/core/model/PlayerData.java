package com.timotheus.core.model;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private int coins;
    private int joins;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.coins = 0;
        this.joins = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public int getJoins() {
        return joins;
    }

    public void incrementJoins() {
        this.joins++;
    }
}