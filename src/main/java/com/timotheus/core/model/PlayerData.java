package com.timotheus.core.model;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public int getJoins() {
        return joins;
    }

    public void setJoins(int joins) {
        this.joins = joins;
    }

    public void incrementJoins() {
        this.joins++;
    }
}
